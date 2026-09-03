# Sistema de Gestión "Epicentro Gourmet" — Hito 1: Hibernate

**Grupo 14** — Licenciatura en Sistemas, Orientación a Objetos II

## De qué se trata

Sistema para administrar festivales gastronómicos: sus unidades de venta (Food Trucks
y Puestos Desarmables), el personal (cocineros y cajeros), los platos, los pedidos y los
costos económicos de cada jornada. El Hito 1 se centra en la **persistencia con Hibernate**:
mapear el modelo de objetos a una base MySQL y hacer consultas.

## Tecnologías usadas

- **Java 21**
- **Maven** — gestión del proyecto y dependencias
- **Hibernate ORM 6.6.4** — mapeo objeto-relacional (ORM)
- **MySQL 8** (driver `mysql-connector-j 8.3.0`)
- **JUnit 5** (Jupiter) — para los casos de uso / tests

## Estructura Maven del proyecto

```
epicentro-gourmet/
├── pom.xml                        <- dependencias y config de build
└── src/
    ├── main/
    │   ├── java/com/grupo14/
    │   │   ├── datos/             <- clases del modelo (entidades)
    │   │   ├── dao/               <- capa de acceso a datos
    │   │   └── util/              <- validaciones y constantes
    │   └── resources/
    │       ├── hibernate.cfg.xml  <- configuración de conexión
    │       └── mapeos/            <- archivos .hbm.xml (mapeo objeto-tabla)
    └── test/
        └── java/com/grupo14/test/ <- casos de uso (uno por integrante)
```

Al ser Maven, cualquiera clona el repo, lo abre en cualquier IDE (IntelliJ, Eclipse,
VS Code) y con `mvn compile` / `mvn test` funciona. No hay archivos de IDE versionados
(los ignoramos con `.gitignore`).

## Las capas

### 1. Datos (el modelo) — `com.grupo14.datos`

Son las clases que representan el dominio. Lo importante para OO:

- **Herencia 1:** `Personal` (abstracta) -> `Cajero` (turno) y `Cocinero` (especialidad + categoría).
- **Herencia 2:** `UnidadVenta` (abstracta) -> `FoodTruck` (patente, conexión eléctrica) y
  `PuestoDesarmable` (carpas, tiempo de montaje).
- **Polimorfismo:** cada subclase implementa métodos propios, como `calcularCannon(festival)` —
  un FoodTruck suma plus de electricidad, un Puesto suma costo de montaje.
- **Relaciones uno-a-muchos:** `Festival -> UnidadVenta`, `UnidadVenta -> staff/platos/pedidos`,
  `Pedido -> DetallePedido`.

### 2. Mapeos — `resources/mapeos/*.hbm.xml`

Le dicen a Hibernate cómo convertir cada clase en una tabla. Lo clave:

- Usamos estrategia **`joined-subclass`** para la herencia: la clase padre tiene su tabla
  (`personal`, `unidad_venta`) y cada subclase tiene la suya unida por la clave
  (`cajero`, `cocinero`, `food_truck`, `puesto_desarmable`).
- Las relaciones se mapean con `<set>` (uno-a-muchos y muchos-a-muchos con tablas intermedias
  como `unidad_venta_staff`) y `<many-to-one>` (el responsable de cada unidad).

### 3. Configuración — `hibernate.cfg.xml`

Define la conexión: driver MySQL, URL (`jdbc:mysql://localhost/tp_grupo14`),
usuario/contraseña, dialecto, y `hbm2ddl.auto=update` (Hibernate crea/actualiza las tablas
solo). Lista qué mapeos cargar.

### 4. DAO (Data Access Object) — `com.grupo14.dao`

La capa que ejecuta las operaciones contra la BD. Diseño:

- **`HibernateUtil`** — singleton que crea una única `SessionFactory` (la conexión "fábrica").
- **`BaseDao`** — clase base con la lógica común: abrir sesión, iniciar transacción
  (`iniciaOperacion()`) y manejar errores con rollback (`manejaExcepcion()`).
- **Un DAO por entidad** (`FoodTruckDao`, `FestivalDao`, etc.) que hereda de `BaseDao` y ofrece:
  - CRUD: `agregar()`, `actualizar()`, `eliminar()`, `traer(id)`.
  - Consultas específicas con **HQL** (lenguaje de consultas de Hibernate),
    ej: `traerPorPatente()`, `traerConConexion()`, `traerPorFestival()`.

Cada método abre una sesión, hace la operación en una transacción, commitea y cierra la sesión.

## Los casos de uso (tests) — uno por integrante

Los hicimos con **JUnit 5**. Cada test **inserta datos de prueba en la BD** y luego
**consulta con Hibernate**, cumpliendo la consigna de usar herencia + uno-a-muchos:

| Test          | Responsable | Qué hace                                                      | Herencia                        | Uno-a-muchos            |
|---------------|-------------|---------------------------------------------------------------|---------------------------------|-------------------------|
| **NachoTest**    | Ignacio     | Lista el staff de una unidad y lo separa en Cajeros/Cocineros | `Personal -> Cajero/Cocinero`      | `UnidadVenta -> staff`     |
| **JoaquinTest**  | Joaquín     | Lista los pedidos de un festival con detalles y recaudación   | `UnidadVenta -> FoodTruck/Puesto`  | `Pedido -> DetallePedido`  |
| **LeandroTest**  | Leandro     | Calcula el canon que paga cada unidad según su tipo           | `UnidadVenta -> FoodTruck/Puesto`  | `Festival -> unidades`     |

Se corren con `mvn test` o desde el IDE. Cada uno imprime el resultado por consola.

## Cómo demostrarlo en vivo

1. Levantar MySQL con la base `tp_grupo14` creada.
2. `mvn test` (o correr cada test desde el IDE).
3. Mostrar la consola con los datos que devuelve cada consulta.
4. Opcional: abrir la BD y mostrar las tablas con los datos insertados.
