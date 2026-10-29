# Defensa — Integrante 1: Seguridad + Panel Admin

Machete para exponer. Cada sección tiene: diagrama, archivo clave, qué decir y la pregunta típica con su respuesta corta.

> Los diagramas están en Mermaid. Se ven en GitHub, GitLab y en VS Code/Kiro con la extensión "Markdown Preview Mermaid Support".

---

## Índice

1. [Mapa general](#1-mapa-general)
2. [Login](#2-login)
3. [CuentaActivaInterceptor](#3-cuentaactivainterceptor)
4. [Redirección por rol y activación de cuenta](#4-redirección-por-rol-y-activación-de-cuenta)
5. [@UnidadDelResponsable](#5-unidaddelresponsable--argument-resolver)
6. [Alta de Unidad de Venta + Responsable](#6-alta-de-unidad-de-venta--responsable)
7. [darDeAlta + Observer (mail)](#7-dardealta--observer-mail)
8. [Estados de la unidad](#8-estados-de-la-unidad-pendiente--habilitada)
9. [Alta de empleados](#9-alta-de-empleados)
10. [Baja lógica](#10-baja-lógica)
11. [Alta de platos](#11-alta-de-platos)
12. [Ranking y mayor canon](#12-ranking-y-mayor-canon)
13. [Polimorfismo del canon](#13-polimorfismo-del-canon)
14. [Factory](#14-factory)
15. [Validaciones custom](#15-validaciones-custom)
16. [Preguntas rápidas](#16-preguntas-rápidas)
17. [Tabla resumen](#17-tabla-resumen)

---

## 1. Mapa general

Recorrido de un request desde que llega hasta que responde.

```mermaid
flowchart LR
    B[Navegador] --> F["SecurityFilterChain<br/>¿logueado? ¿tiene el rol?"]
    F -- "sin sesión" --> L["302 → /login"]
    F -- "sin rol" --> X["403 Forbidden"]
    F -- OK --> I["CuentaActivaInterceptor<br/>¿activo? ¿tiene acceso?"]
    I -- "inactivo" --> CP["/cambiar-password"]
    I -- "baja total" --> SC["/login?sesionCerrada"]
    I -- OK --> R["@UnidadDelResponsable<br/>resuelve la unidad"]
    R --> C[Controller]
    C --> S[Service]
    S --> RP[Repository]
    RP --> DB[(MySQL)]
```



---

## 2. Login

**Archivos:** `SecurityConfiguration.java`, `UsuarioDetailsService.java`

```mermaid
sequenceDiagram
    actor U as Usuario
    participant SF as SecurityFilterChain
    participant UDS as UsuarioDetailsService
    participant DB as PersonalRepository
    participant HC as HomeController

    U->>SF: POST /loginprocess (email, password)
    Note over SF: No hay controller.<br/>Spring intercepta el POST.
    SF->>UDS: loadUserByUsername(email)
    UDS->>DB: findByEmail(email)
    DB-->>UDS: Personal
    UDS->>UDS: roles → SimpleGrantedAuthority<br/>disabled = !tieneAccesoHabilitado
    UDS-->>SF: UserDetails
    SF->>SF: BCrypt compara la password
    alt password OK y no disabled
        SF-->>U: crea sesión, redirige a /dashboard
        U->>HC: GET /dashboard
    else falla
        SF-->>U: /login?error
    end
```

### Reglas de autorización

```java
auth.requestMatchers("/admin/**").hasRole("ADMIN");
auth.requestMatchers("/responsable/**").hasRole("RESPONSABLE");
auth.requestMatchers("/empleado/**").hasAnyRole("EMPLEADO", "RESPONSABLE", "ADMIN");
auth.anyRequest().authenticated();
```

### Qué decir

- `hasRole("ADMIN")` busca la authority `ROLE_ADMIN`. Spring agrega el prefijo `ROLE_` solo. Por eso la tabla `rol` guarda `ROLE_ADMIN`, `ROLE_RESPONSABLE`, `ROLE_EMPLEADO` (constantes en `Roles.java`).
- Sin sesión → 302 a `/login`. Con sesión pero sin rol → 403.
- `loginProcessingUrl("/loginprocess")`: Spring atiende ese POST sin controller.
- Logout: POST a `/logout` → invalida sesión → `/login?logout`.
- **Contra `main`:** allá había `NoOpPasswordEncoder` (texto plano) y CSRF deshabilitado. Acá BCrypt y CSRF activo (el default).

### `tieneAccesoHabilitado`

```java
return !personal.requiereUnidadActiva()
        || asignacionStaffRepository.existsByPersonalIdAndEliminadoFalse(personal.getId());
```

- `requiereUnidadActiva()` es `false` por defecto → admin y responsable siempre entran.
- `Empleado` lo redefine a `true` → necesita al menos una asignación no eliminada.

> **Pregunta:** "¿De dónde saca Spring los usuarios?"
> **Respuesta:** De `loadUserByUsername`. Recibe el email del formulario, busca el `Personal`, convierte sus roles en authorities y devuelve un `UserDetails`. Si el empleado está dado de baja en todas sus unidades, lo marca `disabled` y Spring lo rechaza aunque la contraseña sea correcta.

---

## 3. CuentaActivaInterceptor

**Archivos:** `CuentaActivaInterceptor.java`, `WebConfiguration.java`

```mermaid
flowchart TD
    A[Request entra] --> B{"¿Hay principal?"}
    B -- No --> OK1["return true<br/>(Spring Security ya decidió)"]
    B -- Sí --> C["findByEmail(principal)"]
    C --> D{"¿Existe y<br/>tieneAccesoHabilitado?"}
    D -- No --> E["clearContext()<br/>session.invalidate()"]
    E --> F["redirect /login?sesionCerrada"]
    D -- Sí --> G{"¿isEstaActivo?"}
    G -- No --> H["redirect /cambiar-password"]
    G -- Sí --> OK2["return true → controller"]
```

### Qué decir

- Corre **antes de cualquier controller, en cada request**.
- Dos chequeos:
  1. **Cuenta inactiva** → `/cambiar-password`. El usuario nuevo entra con la contraseña del mail y está obligado a elegir la suya. Así se confirma que el email es suyo.
  2. **Email cambiado o baja en todas las unidades** → cierra la sesión y manda a `/login?sesionCerrada`.
- Se registra excluyendo `/cambiar-password`, `/login`, `/logout`, `/`, `/error` y estáticos. Si no se excluyera `/cambiar-password`, el usuario inactivo entraría en un loop de redirecciones.

> **Pregunta:** "¿Por qué interceptor y no un chequeo en cada controller?"
> **Respuesta:** Porque si está en cada controller, alguno se olvida y el usuario lo saltea escribiendo la URL. Ese era el problema #1 del `main`: `estaActivo` solo se miraba en `/dashboard`, y entrando directo a `/empleado/perfil` se pasaba igual.

---

## 4. Redirección por rol y activación de cuenta

**Archivos:** `HomeController.java`, `PasswordController.java`, `CuentaService.java`

```mermaid
flowchart TD
    D["GET /dashboard"] --> A{"¿rol ADMIN?"}
    A -- Sí --> AF["/admin/festivales"]
    A -- No --> R{"¿rol RESPONSABLE?"}
    R -- Sí --> RD["/responsable/dashboard"]
    R -- No --> EP["/empleado/perfil"]
```

- En `/dashboard` no hace falta mirar `estaActivo`: el interceptor ya lo hizo.

### Activación (`POST /cambiar-password`)

```mermaid
flowchart LR
    P["password + confirmación"] --> V1{"≥ 8 caracteres?"}
    V1 -- No --> ERR[Error en el form]
    V1 -- Sí --> V2{"¿no son solo espacios?"}
    V2 -- No --> ERR
    V2 -- Sí --> V3{"¿coincide la confirmación?"}
    V3 -- No --> ERR
    V3 -- Sí --> V4{"¿la cuenta no estaba activa?"}
    V4 -- No --> ERR
    V4 -- Sí --> OK["BCrypt + estaActivo = true<br/>→ /dashboard"]
```

---

## 5. @UnidadDelResponsable — argument resolver

**Archivos:** `UnidadDelResponsable.java`, `UnidadDelResponsableResolver.java`, `UnidadVentaService.buscarUnidadDelResponsable`

```mermaid
sequenceDiagram
    participant DS as DispatcherServlet
    participant RES as UnidadDelResponsableResolver
    participant UVS as UnidadVentaService
    participant C as ResponsableController

    DS->>DS: ve @UnidadDelResponsable en el parámetro
    DS->>RES: resolveArgument(...)
    RES->>RES: webRequest.getUserPrincipal().getName()
    RES->>UVS: buscarUnidadDelResponsable(email)
    UVS-->>RES: UnidadVenta (findByResponsableEmail)
    RES-->>DS: UnidadVenta
    DS->>C: dashboard(model, unidad)
```

```java
@GetMapping("/dashboard")
public String dashboard(Model model, @UnidadDelResponsable UnidadVenta unidad) { ... }
```

### Qué decir

- El controller recibe la unidad sin saber nada del login.
- La búsqueda por email vive en **un solo lugar**.
- **Por qué importa:** en `main` el responsable veía todo el personal del sistema. Ahora cada operación recibe su unidad y no puede tocar la de otro.

> **Pregunta:** "¿Cómo sabe el responsable cuál es su unidad sin pasar el ID?"
> **Respuesta:** El resolver toma el email del usuario logueado y busca la unidad que tiene ese responsable. El ID nunca viaja en la URL, así que no se puede falsificar.

---

## 6. Alta de Unidad de Venta + Responsable

**Archivo:** `UnidadVentaService.crearUnidadVenta` (`@Transactional`)

```mermaid
flowchart TD
    S["Admin envía CrearUnidadVentaDTO"] --> V0["@Valid: @CodigoUnidad, @Dni,<br/>@MayorDeEdad, @Patente"]
    V0 --> V1{"¿código ya existe?"}
    V1 -- Sí --> E1[ReglaDeNegocioException]
    V1 -- No --> M["mapper.toEntity(dto)<br/>→ TipoUnidad.nuevaUnidad() (Factory)"]
    M --> V2{"¿patente repetida?<br/>(solo FoodTruck)"}
    V2 -- Sí --> E1
    V2 -- No --> V3{"¿el festival ya terminó?"}
    V3 -- Sí --> E1
    V3 -- No --> V4{"validarCanon()<br/>¿canon < 0?"}
    V4 -- Sí --> E1
    V4 -- No --> CR["cuentaService.crearResponsable()<br/>→ darDeAlta"]
    CR --> SV["unidadVentaRepository.save()"]
    SV --> COMMIT((commit))
    E1 --> RB((rollback:<br/>no queda nada))
```

**Frase clave:** "Todo es `@Transactional`. Si falla cualquier paso, no queda ni la unidad ni el responsable a medio guardar."

---

## 7. darDeAlta + Observer (mail)

**Archivos:** `CuentaService.darDeAlta`, `CredencialesGeneradasEvent`, `EmailService`

```mermaid
sequenceDiagram
    participant CS as CuentaService
    participant DB as PersonalRepository
    participant EP as ApplicationEventPublisher
    participant TX as Transacción
    participant ES as EmailService

    CS->>CS: validarEmailLibre / validarDniLibre
    CS->>CS: password = UUID.substring(0, 8)
    CS->>CS: setPass(BCrypt(password))<br/>setEstaActivo(false)<br/>setRoles(...)
    CS->>DB: save(personal)
    CS->>EP: publishEvent(CredencialesGeneradasEvent)
    Note over EP,ES: El mail NO se manda todavía
    TX->>TX: commit
    TX->>ES: @TransactionalEventListener<br/>alGenerarCredenciales(evento)
    ES->>ES: envía mail con password en texto plano
    Note over TX,ES: Si hubo rollback → no se manda nada
```

### Qué decir

- Contraseña aleatoria de 8 caracteres. En la base va **con BCrypt**; en texto plano solo viaja en el mail.
- `estaActivo = false`: entra, pero el interceptor lo manda a elegir su contraseña.
- El evento es un `record` simple: `(email, nombre, password)`.
- Si el mail falla, se loguea y el alta **no** se deshace. El responsable puede pasarle la contraseña a mano.

> **Pregunta:** "¿Por qué `@TransactionalEventListener` y no `@EventListener`?"
> **Respuesta:** Porque se ejecuta después del commit. Si el alta hace rollback, no mandamos credenciales de un usuario que no existe. `fallbackExecution = true` hace que corra igual cuando no hay transacción (por ejemplo, en tests).

---

## 8. Estados de la unidad: Pendiente → Habilitada

```mermaid
stateDiagram-v2
    [*] --> Pendiente: Admin registra la unidad<br/>(mail al responsable)
    Pendiente --> Habilitada: Responsable entra y<br/>elige su contraseña
    note right of Pendiente
        isActiva() = false
        No aparece en ranking ni en canon
    end note
    note right of Habilitada
        isActiva() = true
        responsable.isEstaActivo()
    end note
```

```java
public boolean isActiva() {
    return responsable != null && responsable.isEstaActivo();
}
```

---

## 9. Alta de empleados

**Archivo:** `EmpleadoService.crearEmpleado` / `incorporarEmpleado`

```mermaid
flowchart TD
    A["Responsable envía CrearEmpleadoDTO"] --> U["unidad = @UnidadDelResponsable<br/>(no se elige en el form)"]
    U --> F["personalMapper.toEmpleado()<br/>→ TipoPersonal.nuevoEmpleado() (Factory)"]
    F --> D{"¿existe alguien<br/>con ese DNI?"}
    D -- No --> ALTA["cuentaService.darDeAlta(EMPLEADO)<br/>unidad.agregarAlStaff()"]
    D -- Sí --> E{"¿es Empleado?"}
    E -- No --> X1["Error: el DNI es de alguien<br/>que no es empleado"]
    E -- Sí --> T{"¿activo en mi unidad?"}
    T -- Sí --> X2["Error: ya trabaja en tu unidad"]
    T -- No --> EX["EmpleadoExistenteException<br/>(dado de baja acá o trabajó en otra)"]
    EX --> BTN["Form muestra botón Incorporar"]
    BTN --> INC["incorporarEmpleado()<br/>valida: DNI igual, mismo tipo,<br/>email libre, sin festival superpuesto"]
    INC --> OK["actualiza datos + reactiva en staff<br/>conserva usuario, sin mail nuevo"]
```

> **Pregunta:** "¿Por qué no creás otro usuario si el DNI ya existe?"
> **Respuesta:** Porque es la misma persona. Si Martín trabajó en el Puesto en 2024 y ahora lo quiere el Food Truck, se lo incorpora: conserva su usuario, se actualizan sus datos y queda activo en la nueva unidad.

---

## 10. Baja lógica

```mermaid
erDiagram
    PERSONAL ||--o{ ASIGNACION_STAFF : "tiene"
    UNIDAD_VENTA ||--o{ ASIGNACION_STAFF : "tiene"
    UNIDAD_VENTA ||--o{ PLATO : "ofrece"
    ASIGNACION_STAFF {
        long unidad_id
        long personal_id
        boolean eliminado
    }
    PLATO {
        long id
        string nombre
        boolean eliminado
    }
```

```mermaid
flowchart LR
    M["Martín"] --> A1["Asignación Food Truck<br/>eliminado = true"]
    M --> A2["Asignación Puesto<br/>eliminado = false"]
    A2 --> OK["Sigue entrando al sistema"]
```

### Qué decir

- La baja marca `eliminado = true` en `AsignacionStaff` (la relación unidad-empleado), **no** en `Personal`.
- Es **por unidad**: si lo dan de baja en una, sigue activo en la otra. Solo pierde acceso si está dado de baja en **todas**.
- No se borra porque el empleado tiene historial: festivales, pedidos, recibos.

> **Pregunta:** "¿Por qué baja lógica?"
> **Respuesta:** Un borrado físico se lleva el historial (pedidos, recibos, festivales trabajados). Con la marca, la baja se puede revertir y los reportes viejos siguen cerrando.

---

## 11. Alta de platos

**Archivo:** `PlatoService.crearPlato`

```mermaid
flowchart TD
    A["CrearPlatoDTO"] --> T["nombre.trim()"]
    T --> V{"findFirstByUnidadVentaIdAndNombreIgnoreCase<br/>¿ya existe en mi unidad?"}
    V -- No --> S["Plato.nuevo(...) → save"]
    V -- "Sí, activo" --> E1["Error: ya tenés un plato llamado X"]
    V -- "Sí, dado de baja" --> E2["Error: ... (dado de baja: podés reactivarlo)"]
```

- El nombre se compara sin importar mayúsculas.
- `plato.darDeBaja()` / `plato.reactivar()`.
- Un plato dado de baja **no se ofrece en pedidos nuevos**, pero **sus ventas viejas siguen contando** en los reportes.

---

## 12. Ranking y mayor canon

**Archivo:** `UnidadVentaService`

```mermaid
flowchart LR
    Q["sumarRecaudacionPorUnidad(festivalId)<br/>1 sola query, GROUP BY unidad"] --> MAP["Map&lt;unidadId, total&gt;"]
    U["findByFestivalId(festivalId)"] --> FI["filter(isActiva)"]
    FI --> J["map → RankingUnidadDTO<br/>getOrDefault(id, 0)"]
    MAP --> J
    J --> O["sorted desc por recaudación"]
```

```mermaid
flowchart LR
    T{"top entre 1 y 20?"} -- No --> E[ReglaDeNegocioException]
    T -- Sí --> U["findByFestivalId"] --> FI["filter(isActiva)"] --> C["u.calcularCanon()<br/>(polimorfismo)"] --> O["sorted desc"] --> L["limit(top)"] --> DTO["ReporteMayoresCanonDTO"]
```

### Qué decir

- Ranking: un `Map` armado con **una sola query** evita hacer N consultas (una por unidad).
- Las unidades pendientes no aparecen: todavía no pueden vender.
- `ReporteMayoresCanonDTO` cubre el requisito grupal de "al menos un DTO de reporte".

---

## 13. Polimorfismo del canon

```mermaid
classDiagram
    class UnidadVenta {
        <<abstract>>
        +calcularCanon(ConfiguracionCostos) BigDecimal*
        +validarCanon()
        +isActiva() boolean
    }
    class FoodTruck {
        -boolean estaConectado
        -String patente
        +calcularCanon(config)
    }
    class PuestoDesarmable {
        -int cantidadCarpas
        -float tiempoMontaje
        +calcularCanon(config)
    }
    UnidadVenta <|-- FoodTruck
    UnidadVenta <|-- PuestoDesarmable
```

| Tipo | Fórmula |
|---|---|
| FoodTruck | `superficie × costoSuperficie` + `plusElectricidad` si está conectado |
| PuestoDesarmable | `superficie × costoSuperficie` − `tiempoMontaje × costoMontaje` |

- Sin `instanceof` ni `if (tipo == ...)`. El service llama `u.calcularCanon()` y no sabe qué tipo es.
- Agregar un tercer tipo no toca el service.
- `validarCanon()` rechaza canon negativo (ej.: puesto de 1 m² con 100 min de montaje → 500 − 1000 = −500).

---

## 14. Factory

```mermaid
flowchart LR
    S["String del form<br/>'FOOD_TRUCK'"] --> D["TipoUnidad.desde()"]
    D -- inválido --> E[ReglaDeNegocioException]
    D --> N["nuevaUnidad()"]
    N -- FOOD_TRUCK --> FT[new FoodTruck]
    N -- PUESTO_DESARMABLE --> PD[new PuestoDesarmable]

    S2["'COCINERO'"] --> D2["TipoPersonal.desde()"] --> N2["nuevoEmpleado()"]
    N2 -- COCINERO --> CO[new Cocinero]
    N2 -- CAJERO --> CA[new Cajero]
```

> **Pregunta:** "¿Por qué el enum crea las instancias?"
> **Respuesta:** Porque el enum es el tipo. Si el `switch` estuviera en el service, cada tipo nuevo obliga a acordarse de tocar el service. Así, el que sabe qué subclase corresponde es el propio tipo.

---

## 15. Validaciones custom

Cómo se arma un `ConstraintValidator` (sirve para todas):

```mermaid
flowchart LR
    A["1. Anotación<br/>@Constraint(validatedBy = X.class)"] --> B["2. Clase X implements<br/>ConstraintValidator&lt;Anotacion, Tipo&gt;"]
    B --> C["3. @Valid en el controller<br/>Spring la aplica solo"]
    C --> D["Error marcado en el campo<br/>addPropertyNode(...)"]
```

| Validación | Nivel | Regla | Dónde marca el error |
|---|---|---|---|
| `@CodigoUnidad` | Clase (DTO entero) | `^(FT\|PD)[A-Z0-9]{8}$` y el prefijo coincide con el tipo | `codigo` |
| `@Dni` | Campo | 7 u 8 dígitos | `dni` |
| `@MayorDeEdad` | Campo | nacimiento ≥ 18 años atrás | `fechaNacimiento` |
| `@Patente` *(Integrante 2)* | Campo | `ABC123` o Mercosur `AB123CD`; se normaliza (mayúsculas, sin espacios ni guiones) | `patente` |
| `@IngresoMayorDeEdad` *(Integrante 2)* | Clase (`FechasDeIngreso`) | ingreso no anterior a los 18 años | `fechaIngreso` |

- `@CodigoUnidad` es de clase porque necesita ver `tipoUnidad` para comparar el prefijo.

---

## 16. Preguntas rápidas

| Pregunta | Respuesta en una línea |
|---|---|
| ¿Cómo funciona la seguridad? | Cadena de filtros: autorización por URL, login por formulario sin controller, BCrypt y CSRF activo. |
| ¿Qué pasa si entro sin sesión? | 302 a `/login`. Con sesión y sin rol: 403. |
| ¿Por qué `ROLE_` en la base? | `hasRole("X")` busca la authority `ROLE_X`. |
| ¿Cómo se bloquea a un dado de baja? | `disabled` en `UserDetails` al loguear, y el interceptor le cierra la sesión si ya estaba adentro. |
| ¿Por qué el usuario nuevo no puede operar? | `estaActivo = false` hasta que elige su contraseña; el interceptor lo manda a `/cambiar-password`. |
| ¿Por qué interceptor? | Corre en cada request; no depende de acordarse en cada controller. |
| ¿Cómo evitás que un responsable toque otra unidad? | La unidad sale del usuario logueado (`@UnidadDelResponsable`), nunca de la URL. |
| ¿Cuándo se manda el mail? | Después del commit (`@TransactionalEventListener`). |
| ¿Qué pasa si falla el mail? | Se loguea; el alta queda hecha. |
| ¿Por qué las pendientes no salen en el ranking? | No pueden vender hasta que el responsable active la cuenta. |
| ¿Dónde está el polimorfismo? | `calcularCanon` abstracto, cada subclase su fórmula. |
| ¿Dónde está el Factory? | `TipoUnidad.nuevaUnidad()` y `TipoPersonal.nuevoEmpleado()`. |
| ¿Dónde está el Observer? | `CredencialesGeneradasEvent` publicado en `darDeAlta`, escuchado por `EmailService`. |
| ¿La baja es global? | No, es por unidad (`AsignacionStaff.eliminado`). |
| ¿Un plato dado de baja afecta reportes? | No; deja de ofrecerse en pedidos nuevos, sus ventas viejas siguen contando. |

---

## 17. Tabla resumen

| Tema | Archivo clave | Qué decir |
|---|---|---|
| Login | `SecurityConfiguration` | Cadena de filtros, autorización por URL, BCrypt, CSRF activo |
| UsuarioDetailsService | `UsuarioDetailsService` | Busca por email, roles → authorities, `disabled` si no tiene acceso |
| Interceptor | `CuentaActivaInterceptor` | Antes de todo controller; inactivo → `/cambiar-password`; baja → cierra sesión |
| Argument resolver | `UnidadDelResponsableResolver` | Resuelve la unidad del logueado; el service no sabe del login |
| Ranking | `UnidadVentaService.obtenerRankingPorFestival` | Filtra `isActiva`, una query + Map, orden descendente |
| Mayor canon | `UnidadVentaService.obtenerMayoresCanon` | Filtra `isActiva`, polimorfismo, top 1–20 |
| Polimorfismo | `FoodTruck` / `PuestoDesarmable` `.calcularCanon` | Sin `instanceof`, cada subclase su fórmula |
| Factory | `TipoUnidad.nuevaUnidad()` / `TipoPersonal.nuevoEmpleado()` | El enum crea la instancia |
| Observer | `CredencialesGeneradasEvent` + `EmailService` | Mail después del commit |
| Alta empleado | `EmpleadoService.crearEmpleado` | DNI repetido → `EmpleadoExistenteException` → incorporar |
| Baja lógica | `AsignacionStaff.eliminado` / `Plato.eliminado` | Por unidad, no global; marca, no borra |
| Validaciones | `CodigoUnidadValidator`, `PatenteValidator`, `IngresoMayorDeEdadValidator` | `ConstraintValidator` custom, error marcado en el campo |

---

## Orden sugerido para exponer (≈ 10 min)

1. Mapa general (sección 1) — 1 min
2. Login + interceptor (2 y 3) — 2 min
3. `@UnidadDelResponsable` (5) — 1 min
4. Alta de unidad + Observer + estados (6, 7, 8) — 2 min
5. Alta de empleados + baja lógica (9, 10) — 2 min
6. Ranking, canon, polimorfismo, Factory (12, 13, 14) — 2 min
7. Validaciones solo si preguntan (15)
