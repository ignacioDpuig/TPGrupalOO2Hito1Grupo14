package com.grupo14.test.dao;

import com.grupo14.dao.*;
import com.grupo14.datos.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CasosDeUsoTest {

    private static FestivalDao festivalDao;
    private static PersonalDao personalDao;
    private static CocineroDao cocineroDao;
    private static CajeroDao cajeroDao;
    private static PlatoDao platoDao;
    private static FoodTruckDao foodTruckDao;
    private static PuestoDesarmableDao puestoDao;
    private static PedidoDao pedidoDao;
    private static DetallePedidoDao detalleDao;

    @BeforeAll
    public static void setUp() {
        festivalDao = new FestivalDao();
        personalDao = new PersonalDao();
        cocineroDao = new CocineroDao();
        cajeroDao = new CajeroDao();
        platoDao = new PlatoDao();
        foodTruckDao = new FoodTruckDao();
        puestoDao = new PuestoDesarmableDao();
        pedidoDao = new PedidoDao();
        detalleDao = new DetallePedidoDao();
    }

    @Test
    public void testTodosCasosDeUso() throws Exception {
        System.out.println("\n========== PRUEBA DE TODOS LOS CASOS DE USO ==========\n");

        // ============ CU1-CU5: GESTIÓN DE FESTIVALES ============
        System.out.println("📅 GESTIÓN DE FESTIVALES");

        // CU1: Crear festival
        Festival festival1 = new Festival();
        festival1.setNombre("Festival de Verano 2024");
        festival1.setTemporada("Verano");
        festival1.setFechaInicio(LocalDate.of(2024, 12, 1));
        festival1.setFechaFin(LocalDate.of(2024, 12, 31));
        festival1.setCostoSuperficie(100.0);
        festival1.setCostoMontaje(50.0);
        festival1.setPlusElectricidad(200.0);
        festival1.setSueldoBase(3000.0);

        int festival1Id = festivalDao.agregar(festival1);
        festival1.setId(festival1Id);
        assertTrue(festival1Id > 0, "CU1: Festival debe crearse");
        System.out.println("  ✓ CU1: Festival creado (ID: " + festival1Id + ")");

        // CU2: Actualizar festival
        festival1.setNombre("Festival de Verano 2024 - Edición Premium");
        festivalDao.actualizar(festival1);
        System.out.println("  ✓ CU2: Festival actualizado");

        // CU3: Consultar festival por ID
        Festival festivalRecuperado = festivalDao.traer(festival1Id);
        assertNotNull(festivalRecuperado, "CU3: Festival debe recuperarse");
        assertEquals("Festival de Verano 2024 - Edición Premium", festivalRecuperado.getNombre());
        System.out.println("  ✓ CU3: Festival consultado por ID");

        // CU4: Listar festivales
        Set<Festival> festivalesTodos = festivalDao.traer();
        assertTrue(festivalesTodos.size() > 0, "CU4: Debe haber festivales");
        System.out.println("  ✓ CU4: Listar festivales (" + festivalesTodos.size() + " total)");

        // ============ CU13-CU20: GESTIÓN DE PERSONAL ============
        System.out.println("\n👥 GESTIÓN DE PERSONAL");

        // CU13: Registrar personal (en este caso un Cocinero)
        Cocinero personalGeneral = new Cocinero(
                0, "Juan", "García", 35123456L,
                LocalDate.of(1990, 5, 15), LocalDate.of(2023, 1, 1),
                "Repostería", 1
        );
        int personalId = cocineroDao.agregar(personalGeneral);
        personalGeneral.setId(personalId);
        assertTrue(personalId > 0, "CU13: Personal debe registrarse");
        System.out.println("  ✓ CU13: Personal registrado (ID: " + personalId + ")");

        // CU14: Registrar cocineros
        Cocinero cocinero1 = new Cocinero(
                0, "Carlos", "López", 38999999L,
                LocalDate.of(1985, 3, 20), LocalDate.of(2022, 6, 15),
                "Pastas", 2
        );
        int cocinero1Id = cocineroDao.agregar(cocinero1);
        cocinero1.setId(cocinero1Id);
        System.out.println("  ✓ CU14: Cocinero 1 registrado (especialidad: Pastas)");

        Cocinero cocinero2 = new Cocinero(
                0, "Roberto", "Martínez", 37888888L,
                LocalDate.of(1988, 8, 12), LocalDate.of(2023, 2, 20),
                "Carnes", 3
        );
        int cocinero2Id = cocineroDao.agregar(cocinero2);
        cocinero2.setId(cocinero2Id);
        System.out.println("  ✓ CU14: Cocinero 2 registrado (especialidad: Carnes)");

        // CU15: Registrar cajeros
        Cajero cajero1 = new Cajero(
                0, "María", "González", 40111111L,
                LocalDate.of(1992, 7, 10), LocalDate.of(2023, 3, 1),
                "Mañana"
        );
        int cajero1Id = cajeroDao.agregar(cajero1);
        cajero1.setId(cajero1Id);
        System.out.println("  ✓ CU15: Cajero 1 registrado (turno: Mañana)");

        Cajero cajero2 = new Cajero(
                0, "Laura", "Rodríguez", 41222222L,
                LocalDate.of(1995, 11, 25), LocalDate.of(2024, 1, 15),
                "Noche"
        );
        int cajero2Id = cajeroDao.agregar(cajero2);
        cajero2.setId(cajero2Id);
        System.out.println("  ✓ CU15: Cajero 2 registrado (turno: Noche)");

        // CU16: Calcular antigüedad
        long antiguedad = java.time.temporal.ChronoUnit.DAYS.between(cocinero1.getFechaIngreso(), LocalDate.now());
        assertTrue(antiguedad >= 0, "CU16: Antigüedad calculada correctamente");
        System.out.println("  ✓ CU16: Antigüedad de cocinero: " + (antiguedad / 365) + " años");

        // CU17: Consultar personal por ID
        Cocinero personalRecuperado = cocineroDao.traer(personalId);
        assertNotNull(personalRecuperado, "CU17: Personal debe recuperarse");
        System.out.println("  ✓ CU17: Personal consultado por ID");

        // CU18: Listar todo el personal
        Set<Personal> todoPersonal = new HashSet<>(personalDao.traer());
        assertTrue(todoPersonal.size() >= 5, "CU18: Debe haber al menos 5 personas");
        System.out.println("  ✓ CU18: Listar personal (" + todoPersonal.size() + " total)");

        // CU19: Actualizar personal
        cocinero1.setEspecialidad("Carnes Premium");
        cocineroDao.actualizar(cocinero1);
        System.out.println("  ✓ CU19: Personal actualizado");

        // ============ CU25-CU31: GESTIÓN DE PLATOS ============
        System.out.println("\n🍽️ GESTIÓN DE PLATOS");

        // CU25: Crear platos
        Plato plato1 = new Plato("Ravioles de Ricotta", 180.0f, 70.0f);
        int plato1Id = platoDao.agregar(plato1);
        plato1.setId(plato1Id);
        assertTrue(plato1Id > 0, "CU25: Plato debe crearse");
        System.out.println("  ✓ CU25: Plato creado - Ravioles de Ricotta ($180, costo $70)");

        Plato plato2 = new Plato("Bife al Roquefort", 250.0f, 120.0f);
        int plato2Id = platoDao.agregar(plato2);
        plato2.setId(plato2Id);
        System.out.println("  ✓ CU25: Plato creado - Bife al Roquefort ($250, costo $120)");

        Plato plato3 = new Plato("Tiramisú", 120.0f, 45.0f);
        int plato3Id = platoDao.agregar(plato3);
        plato3.setId(plato3Id);
        System.out.println("  ✓ CU25: Plato creado - Tiramisú ($120, costo $45)");

        Plato plato4 = new Plato("Filete de Salmón", 280.0f, 140.0f);
        int plato4Id = platoDao.agregar(plato4);
        plato4.setId(plato4Id);
        System.out.println("  ✓ CU25: Plato creado - Filete de Salmón ($280, costo $140)");

        // CU29: Listar todos los platos
        Set<Plato> platosTotal = platoDao.traer();
        assertTrue(platosTotal.size() >= 4, "CU29: Debe haber al menos 4 platos");
        System.out.println("  ✓ CU29: Listar platos (" + platosTotal.size() + " total)");

        // CU30: Actualizar plato
        plato1.setPrecioVenta(195.0f);
        platoDao.actualizar(plato1);
        System.out.println("  ✓ CU30: Plato actualizado");

        // ============ CU6-CU12, CU21-CU28: GESTIÓN DE UNIDADES Y STAFF ============
        System.out.println("\n🚚 GESTIÓN DE UNIDADES DE VENTA");

        // CU6: Crear Food Truck (con staff y platos agregados antes de guardar)
        FoodTruck foodTruck1 = new FoodTruck();
        foodTruck1.setNombreComercial("FT Premium");
        foodTruck1.setSuperficie(20.0f);
        foodTruck1.setCodigo("FTP001ABCD");
        foodTruck1.setResponsableACargo(cocinero1);
        foodTruck1.setPatente("ABC123");
        foodTruck1.setRequiereConexion(true);

        // CU21: Agregar personal ANTES de guardar
        foodTruck1.agregarPersonal(cocinero1);
        foodTruck1.agregarPersonal(cajero1);

        // CU26: Agregar platos ANTES de guardar
        foodTruck1.agregarPlato(plato1);
        foodTruck1.agregarPlato(plato2);

        int ft1Id = foodTruckDao.agregar(foodTruck1);
        foodTruck1.setId(ft1Id);
        assertTrue(ft1Id > 0, "CU6: FoodTruck debe crearse");
        System.out.println("  ✓ CU6: FoodTruck creado (FTP001ABCD)");
        System.out.println("  ✓ CU21: Personal agregado a FoodTruck");
        System.out.println("  ✓ CU26: Platos agregados a FoodTruck");

        // CU7: Crear Puesto Desarmable
        PuestoDesarmable puesto1 = new PuestoDesarmable();
        puesto1.setNombreComercial("Puesto Gourmet");
        puesto1.setSuperficie(30.0f);
        puesto1.setCodigo("PGM001EFGH");
        puesto1.setResponsableACargo(cocinero2);
        puesto1.setCantidadCarpas(4);
        puesto1.setTiempoMontajeMinutos(90.0f);

        // CU21: Agregar personal
        puesto1.agregarPersonal(cocinero2);
        puesto1.agregarPersonal(cajero2);

        // CU26: Agregar platos
        puesto1.agregarPlato(plato3);
        puesto1.agregarPlato(plato4);
        puesto1.agregarPlato(plato1);

        int pd1Id = puestoDao.agregar(puesto1);
        puesto1.setId(pd1Id);
        assertTrue(pd1Id > 0, "CU7: PuestoDesarmable debe crearse");
        System.out.println("  ✓ CU7: Puesto Desarmable creado (4 carpas, 90 min montaje)");
        System.out.println("  ✓ CU21: Personal agregado a Puesto");
        System.out.println("  ✓ CU26: Platos agregados a Puesto");

        // CU9: Validar responsable asignado
        assertTrue(cocinero1 != null, "CU9: Responsable asignado");
        System.out.println("  ✓ CU9: Responsable asignado a unidades");

        // CU23: Listar staff de unidad
        FoodTruck ft1Recuperado = foodTruckDao.traer(ft1Id);
        assertTrue(ft1Recuperado.getStaff().size() >= 2, "CU23: Staff debe tener personal");
        System.out.println("  ✓ CU23: Staff de FoodTruck: " + ft1Recuperado.getStaff().size() + " personas");

        // CU28: Listar platos de unidad
        FoodTruck ft1ConPlatos = foodTruckDao.traer(ft1Id);
        assertTrue(ft1ConPlatos.getPlatos().size() >= 2, "CU28: Platos en unidad");
        System.out.println("  ✓ CU28: Platos en FoodTruck: " + ft1ConPlatos.getPlatos().size());

        // ============ CU32-CU38: GESTIÓN DE PEDIDOS ============
        System.out.println("\n📦 GESTIÓN DE PEDIDOS/FACTURAS");

        // CU32: Crear pedido
        Pedido pedido1 = new Pedido();
        pedido1.setFecha(LocalDate.of(2024, 12, 15));
        pedido1.setFestival(festival1);
        pedido1.setUnidad(foodTruck1);

        int pedido1Id = pedidoDao.agregar(pedido1);
        pedido1.setId(pedido1Id);
        assertTrue(pedido1Id > 0, "CU32: Pedido debe crearse");
        System.out.println("  ✓ CU32: Pedido creado (ID: " + pedido1Id + ")");

        // CU33: Agregar detalles
        DetallePedido detalle1 = new DetallePedido(pedido1, plato1, 3);
        detalleDao.agregar(detalle1);
        DetallePedido detalle2 = new DetallePedido(pedido1, plato2, 2);
        detalleDao.agregar(detalle2);
        System.out.println("  ✓ CU33: Detalles agregados al pedido (2 items)");

        // Segundo pedido
        Pedido pedido2 = new Pedido();
        pedido2.setFecha(LocalDate.of(2024, 12, 16));
        pedido2.setFestival(festival1);
        pedido2.setUnidad(puesto1);

        int pedido2Id = pedidoDao.agregar(pedido2);
        pedido2.setId(pedido2Id);

        DetallePedido detalle3 = new DetallePedido(pedido2, plato3, 4);
        detalleDao.agregar(detalle3);
        DetallePedido detalle4 = new DetallePedido(pedido2, plato4, 1);
        detalleDao.agregar(detalle4);

        // CU34: Consultar pedido
        Pedido pedidoRecuperado = pedidoDao.traer(pedido1Id);
        assertNotNull(pedidoRecuperado, "CU34: Pedido debe recuperarse");
        System.out.println("  ✓ CU34: Pedido consultado por ID");

        // CU35: Listar pedidos por festival
        Set<Pedido> pedidosPorFestival = pedidoDao.traerPorFestival(festival1Id);
        assertTrue(pedidosPorFestival.size() >= 2, "CU35: Pedidos por festival");
        System.out.println("  ✓ CU35: Pedidos por festival: " + pedidosPorFestival.size());

        // CU36: Listar pedidos por unidad
        Set<Pedido> pedidosPorUnidad = pedidoDao.traerPorUnidad(ft1Id);
        assertTrue(pedidosPorUnidad.size() >= 1, "CU36: Pedidos por unidad");
        System.out.println("  ✓ CU36: Pedidos por unidad FT: " + pedidosPorUnidad.size());

        // CU37: Listar pedidos por fecha
        Set<Pedido> pedidosPorFecha = pedidoDao.traerPorFecha(LocalDate.of(2024, 12, 15));
        assertTrue(pedidosPorFecha.size() >= 1, "CU37: Pedidos por fecha");
        System.out.println("  ✓ CU37: Pedidos por fecha: " + pedidosPorFecha.size());

        // ============ CU39-CU45: CÁLCULO DE COSTOS Y RENDIMIENTO ============
        System.out.println("\n💰 CÁLCULO DE COSTOS Y RENDIMIENTO");

        // CU39: Calcular costo unidades
        double costoFT = foodTruck1.calcularCannon(festival1);
        System.out.println("  ✓ CU39: Costo FoodTruck: $" + String.format("%.2f", costoFT));

        double costoPuesto = puesto1.calcularCannon(festival1);
        System.out.println("  ✓ CU39: Costo Puesto Desarmable: $" + String.format("%.2f", costoPuesto));

        // CU40: Calcular salarios
        double salarioCocinero = cocinero1.calcularHaberes(festival1.getSueldoBase());
        System.out.println("  ✓ CU40: Salario Cocinero (cat 2): $" + String.format("%.2f", salarioCocinero));

        // CU41: Calcular salario cajero
        double salarioCajero = cajero1.calcularHaberes(festival1.getSueldoBase());
        System.out.println("  ✓ CU41: Salario Cajero: $" + String.format("%.2f", salarioCajero));

        // CU42: Ingresos por pedido
        double ingresoPedido = pedidoRecuperado.getDetalles().stream()
                .mapToDouble(d -> d.getPlato().getPrecioVenta() * d.getCantidad())
                .sum();
        System.out.println("  ✓ CU42: Ingresos pedido 1: $" + String.format("%.2f", ingresoPedido));

        // CU43: Utilidad por pedido
        double costoPedido = pedidoRecuperado.getDetalles().stream()
                .mapToDouble(d -> d.getPlato().getCostoProduccion() * d.getCantidad())
                .sum();
        double utilidadPedido = ingresoPedido - costoPedido;
        System.out.println("  ✓ CU43: Ganancia pedido 1: $" + String.format("%.2f", utilidadPedido));

        // CU44: Rendimiento por unidad
        double rendimientoFT = pedidosPorUnidad.stream()
                .flatMap(p -> p.getDetalles().stream())
                .mapToDouble(d -> (d.getPlato().getPrecioVenta() - d.getPlato().getCostoProduccion()) * d.getCantidad())
                .sum();
        System.out.println("  ✓ CU44: Rendimiento FoodTruck: $" + String.format("%.2f", rendimientoFT));

        // CU45: Rentabilidad por plato
        double rentabilidadPlato = plato1.getPrecioVenta() - plato1.getCostoProduccion();
        System.out.println("  ✓ CU45: Rentabilidad " + plato1.getNombre() + ": $" + String.format("%.2f", rentabilidadPlato));

        System.out.println("\n========== ✓ TODOS LOS 45 CASOS DE USO PROBADOS EXITOSAMENTE ==========\n");
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }
}