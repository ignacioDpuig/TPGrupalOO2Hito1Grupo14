package com.grupo14.datos;

import com.grupo14.dao.CajeroDao;
import com.grupo14.dao.CocineroDao;
import com.grupo14.dao.DetallePedidoDao;
import com.grupo14.dao.FestivalDao;
import com.grupo14.dao.FoodTruckDao;
import com.grupo14.dao.PedidoDao;
import com.grupo14.dao.PlatoDao;
import com.grupo14.dao.PuestoDesarmableDao;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase de carga de datos (seed) para poblar la base con un conjunto
 * inicial compartido por todo el grupo.
 *
 * Uso tipico desde un test o un main:
 *
 *     DatosIniciales.Resultado datos = DatosIniciales.cargar();
 *     Festival f = datos.festival;
 *
 * Como el hibernate.cfg.xml usa hbm2ddl.auto=create, cada corrida
 * recrea las tablas vacias antes de insertar estos datos, de modo que
 * todos los integrantes parten del mismo estado.
 *
 * Contenido sembrado:
 *
 *  FESTIVAL PRINCIPAL ("Festival Canon"):
 *   - 4 integrantes de personal (2 cocineros y 2 cajeros).
 *   - 3 platos (compartidos entre unidades).
 *   - 1 FoodTruck (con staff, platos y requiere conexion electrica).
 *   - 1 PuestoDesarmable (con staff y platos).
 *   - 3 pedidos: 2 en el FoodTruck y 1 en el Puesto.
 *
 *  SEGUNDO FESTIVAL ("Festival Invierno"):
 *   - 1 FoodTruck con conexion electrica.
 *   - 1 pedido.
 *   Sirve para probar que las consultas por festival filtran bien
 *   y no mezclan datos entre festivales distintos.
 *
 * Esta clase NO valida nada: solo crea y persiste. Las validaciones
 * quedan del lado de los tests.
 */
public class DatosIniciales {

    /**
     * Contenedor con las entidades creadas, para que quien llame
     * pueda hacer aserciones o seguir operando sobre ellas.
     */
    public static class Resultado {
        // --- Festival principal ---
        public Festival festival;

        public FoodTruck foodTruck;
        public PuestoDesarmable puesto;

        public Cocinero cocineroFoodTruck;
        public Cajero cajeroFoodTruck;
        public Cocinero cocineroPuesto;
        public Cajero cajeroPuesto;

        public Plato platoRavioles;
        public Plato platoAsado;
        public Plato platoEmpanadas;

        // Pedidos del festival principal:
        // dos en el FoodTruck y uno en el Puesto.
        public Pedido pedidoFoodTruck1;
        public Pedido pedidoFoodTruck2;
        public Pedido pedidoPuesto;

        // --- Segundo festival (para probar filtrado por festival) ---
        public Festival festivalSecundario;
        public FoodTruck foodTruckSecundario;
        public Pedido pedidoFestivalSecundario;
    }

    /**
     * Crea y persiste el conjunto de datos inicial.
     *
     * Orden de persistencia: primero el Festival, luego el personal y los
     * platos, despues las unidades de venta (que referencian festival, staff
     * y platos) y por ultimo los pedidos con sus detalles.
     *
     * @return las entidades creadas, ya con su id asignado.
     * @throws Exception si falla la construccion de alguna entidad.
     */
    public static Resultado cargar() throws Exception {
        FestivalDao festivalDao = new FestivalDao();
        CocineroDao cocineroDao = new CocineroDao();
        CajeroDao cajeroDao = new CajeroDao();
        PlatoDao platoDao = new PlatoDao();
        FoodTruckDao foodTruckDao = new FoodTruckDao();
        PuestoDesarmableDao puestoDao = new PuestoDesarmableDao();
        PedidoDao pedidoDao = new PedidoDao();
        DetallePedidoDao detalleDao = new DetallePedidoDao();

        Resultado r = new Resultado();

        // =====================================================================
        // FESTIVAL PRINCIPAL
        // =====================================================================
        Festival festival = new Festival();
        festival.setNombre("Festival Canon");
        festival.setTemporada("Verano");
        festival.setFechaInicio(LocalDate.of(2026, 1, 10));
        festival.setFechaFin(LocalDate.of(2026, 1, 20));
        festival.setCostoSuperficie(100.0);
        festival.setCostoMontaje(10.0);
        festival.setPlusElectricidad(500.0);
        festival.setSueldoBase(1000.0);
        festival.setUnidades(new HashSet<>());
        festival.setId(festivalDao.agregar(festival));
        r.festival = festival;

        // --- Personal del FoodTruck ---
        Cocinero cocineroFt = new Cocinero(
                0, "Mario", "Rossi", 30111222L,
                LocalDate.of(1988, 5, 10), LocalDate.now().minusYears(6),
                "Hamburguesas", 2
        );
        cocineroFt.setId(cocineroDao.agregar(cocineroFt));
        r.cocineroFoodTruck = cocineroFt;

        Cajero cajeroFt = new Cajero(
                0, "Ana", "Lopez", 32111333L,
                LocalDate.of(1990, 3, 22), LocalDate.now().minusYears(4),
                "MANANA"
        );
        cajeroFt.setId(cajeroDao.agregar(cajeroFt));
        r.cajeroFoodTruck = cajeroFt;

        // --- Personal del PuestoDesarmable ---
        Cocinero cocineroPd = new Cocinero(
                0, "Lucia", "Perez", 33111444L,
                LocalDate.of(1987, 8, 15), LocalDate.now().minusYears(3),
                "Pizzas", 1
        );
        cocineroPd.setId(cocineroDao.agregar(cocineroPd));
        r.cocineroPuesto = cocineroPd;

        Cajero cajeroPd = new Cajero(
                0, "Juan", "Gomez", 34111555L,
                LocalDate.of(1992, 12, 1), LocalDate.now().minusYears(2),
                "NOCHE"
        );
        cajeroPd.setId(cajeroDao.agregar(cajeroPd));
        r.cajeroPuesto = cajeroPd;

        // --- Platos (compartidos entre unidades) ---
        Plato ravioles = new Plato("Ravioles de la casa", 300.0f, 120.0f);
        ravioles.setId(platoDao.agregar(ravioles));
        r.platoRavioles = ravioles;

        Plato asadoTira = new Plato("Asado de tira", 450.0f, 200.0f);
        asadoTira.setId(platoDao.agregar(asadoTira));
        r.platoAsado = asadoTira;

        Plato empanadas = new Plato("Empanadas x6", 180.0f, 70.0f);
        empanadas.setId(platoDao.agregar(empanadas));
        r.platoEmpanadas = empanadas;

        // --- FoodTruck (requiere conexion electrica) ---
        // El staff se arma primero: el responsable debe pertenecer al staff.
        Set<Personal> staffFt = new HashSet<>();
        staffFt.add(cocineroFt);
        staffFt.add(cajeroFt);

        Set<Plato> platosFt = new HashSet<>();
        platosFt.add(ravioles);
        platosFt.add(empanadas);

        FoodTruck foodTruck = new FoodTruck(
                0, "BurgerBus", cocineroFt, 20.0f,
                staffFt, "ABC123", true,
                new HashSet<>(), platosFt, festival
        );
        festival.agregarUnidad(foodTruck);
        foodTruck.setId(foodTruckDao.agregar(foodTruck));
        r.foodTruck = foodTruck;

        // --- PuestoDesarmable (60 minutos de montaje) ---
        Set<Personal> staffPd = new HashSet<>();
        staffPd.add(cocineroPd);
        staffPd.add(cajeroPd);

        Set<Plato> platosPd = new HashSet<>();
        platosPd.add(asadoTira);
        platosPd.add(empanadas);

        PuestoDesarmable puesto = new PuestoDesarmable(
                0, "PizzaSur", cocineroPd, 15.0f,
                staffPd, 2, 60.0f,
                new HashSet<>(), platosPd, festival
        );
        festival.agregarUnidad(puesto);
        puesto.setId(puestoDao.agregar(puesto));
        r.puesto = puesto;

        // --- Pedido 1 del FoodTruck (2 detalles) ---
        // El pedido se guarda primero para obtener su id; recien despues se
        // crean los detalles con el pedido ya seteado, porque en el mapeo el
        // DetallePedido tiene id compuesto (pedido + plato) y necesita el id.
        Pedido pedidoFt1 = new Pedido();
        pedidoFt1.setFecha(LocalDate.of(2026, 1, 12));
        pedidoFt1.setUnidad(foodTruck);
        pedidoFt1.setFestival(festival);
        pedidoFt1.setId(pedidoDao.agregar(pedidoFt1));
        detalleDao.agregar(new DetallePedido(pedidoFt1, ravioles, 5));
        detalleDao.agregar(new DetallePedido(pedidoFt1, empanadas, 3));
        r.pedidoFoodTruck1 = pedidoFt1;

        // --- Pedido 2 del FoodTruck (1 detalle) ---
        Pedido pedidoFt2 = new Pedido();
        pedidoFt2.setFecha(LocalDate.of(2026, 1, 14));
        pedidoFt2.setUnidad(foodTruck);
        pedidoFt2.setFestival(festival);
        pedidoFt2.setId(pedidoDao.agregar(pedidoFt2));
        detalleDao.agregar(new DetallePedido(pedidoFt2, ravioles, 2));
        r.pedidoFoodTruck2 = pedidoFt2;

        // --- Pedido del Puesto (2 detalles) ---
        Pedido pedidoPd = new Pedido();
        pedidoPd.setFecha(LocalDate.of(2026, 1, 13));
        pedidoPd.setUnidad(puesto);
        pedidoPd.setFestival(festival);
        pedidoPd.setId(pedidoDao.agregar(pedidoPd));
        detalleDao.agregar(new DetallePedido(pedidoPd, asadoTira, 4));
        detalleDao.agregar(new DetallePedido(pedidoPd, empanadas, 6));
        r.pedidoPuesto = pedidoPd;

        // =====================================================================
        // SEGUNDO FESTIVAL (para verificar el filtrado por festival)
        // =====================================================================
        Festival festival2 = new Festival();
        festival2.setNombre("Festival Invierno");
        festival2.setTemporada("Invierno");
        festival2.setFechaInicio(LocalDate.of(2026, 7, 5));
        festival2.setFechaFin(LocalDate.of(2026, 7, 15));
        festival2.setCostoSuperficie(95.0);
        festival2.setCostoMontaje(50.0);
        festival2.setPlusElectricidad(200.0);
        festival2.setSueldoBase(1200.0);
        festival2.setUnidades(new HashSet<>());
        festival2.setId(festivalDao.agregar(festival2));
        r.festivalSecundario = festival2;

        // --- Personal del FoodTruck del segundo festival ---
        Cocinero cocineroFt2 = new Cocinero(
                0, "Sofia", "Diaz", 35111666L,
                LocalDate.of(1991, 4, 4), LocalDate.now().minusYears(5),
                "Tacos", 2
        );
        cocineroFt2.setId(cocineroDao.agregar(cocineroFt2));

        Cajero cajeroFt2 = new Cajero(
                0, "Pedro", "Ramirez", 36111777L,
                LocalDate.of(1994, 9, 9), LocalDate.now().minusYears(1),
                "TARDE"
        );
        cajeroFt2.setId(cajeroDao.agregar(cajeroFt2));

        Set<Personal> staffFt2 = new HashSet<>();
        staffFt2.add(cocineroFt2);
        staffFt2.add(cajeroFt2);

        Set<Plato> platosFt2 = new HashSet<>();
        platosFt2.add(empanadas);

        FoodTruck foodTruck2 = new FoodTruck(
                0, "TacoMovil", cocineroFt2, 14.0f,
                staffFt2, "XYZ789", true,
                new HashSet<>(), platosFt2, festival2
        );
        festival2.agregarUnidad(foodTruck2);
        foodTruck2.setId(foodTruckDao.agregar(foodTruck2));
        r.foodTruckSecundario = foodTruck2;

        // --- Pedido del segundo festival (1 detalle) ---
        Pedido pedidoFest2 = new Pedido();
        pedidoFest2.setFecha(LocalDate.of(2026, 7, 6));
        pedidoFest2.setUnidad(foodTruck2);
        pedidoFest2.setFestival(festival2);
        pedidoFest2.setId(pedidoDao.agregar(pedidoFest2));
        detalleDao.agregar(new DetallePedido(pedidoFest2, empanadas, 10));
        r.pedidoFestivalSecundario = pedidoFest2;

        return r;
    }

    /**
     * Permite correr la carga directamente como aplicacion Java.
     */
    public static void main(String[] args) throws Exception {
        Resultado r = cargar();
        System.out.println("Datos iniciales cargados:");
        System.out.println("  Festival principal id=" + r.festival.getId() + " -> " + r.festival.getNombre());
        System.out.println("    FoodTruck id=" + r.foodTruck.getId() + " -> " + r.foodTruck.getNombreComercial() + " (2 pedidos)");
        System.out.println("    Puesto    id=" + r.puesto.getId() + " -> " + r.puesto.getNombreComercial() + " (1 pedido)");
        System.out.println("    Platos    : 3 (ravioles, asado, empanadas)");
        System.out.println("  Festival secundario id=" + r.festivalSecundario.getId() + " -> " + r.festivalSecundario.getNombre());
        System.out.println("    FoodTruck id=" + r.foodTruckSecundario.getId() + " -> " + r.foodTruckSecundario.getNombreComercial() + " (1 pedido)");
    }

    private DatosIniciales() {
        // Clase de utilidad: no se instancia.
    }
}
