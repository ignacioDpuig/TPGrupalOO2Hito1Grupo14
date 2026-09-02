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

public class IntegracionCompleteTest {

    private static FestivalDao festivalDao;
    private static CocineroDao cocineroDao;
    private static CajeroDao cajeroDao;
    private static PlatoDao platoDao;
    private static FoodTruckDao foodTruckDao;
    private static PuestoDesarmableDao puestoDao;
    private static PedidoDao pedidoDao;
    private static DetallePedidoDao detalleDao;
    private static PersonalDao personalDao;

    @BeforeAll
    public static void setUp() {
        festivalDao = new FestivalDao();
        cocineroDao = new CocineroDao();
        cajeroDao = new CajeroDao();
        platoDao = new PlatoDao();
        foodTruckDao = new FoodTruckDao();
        puestoDao = new PuestoDesarmableDao();
        pedidoDao = new PedidoDao();
        detalleDao = new DetallePedidoDao();
        personalDao = new PersonalDao();
    }

    @Test
    public void testFlujoCompleto() throws Exception {
        // 1. Crear Festival con costos
        Festival festival = new Festival();
        festival.setNombre("Festival Gourmet 2024");
        festival.setTemporada("Verano");
        festival.setFechaInicio(LocalDate.of(2024, 12, 1));
        festival.setFechaFin(LocalDate.of(2024, 12, 31));
        festival.setCostoSuperficie(100.0);
        festival.setCostoMontaje(50.0);
        festival.setPlusElectricidad(200.0);
        festival.setSueldoBase(3000.0);

        int festivalId = festivalDao.agregar(festival);
        festival.setId(festivalId);
        assertTrue(festivalId > 0, "Festival debe guardarse");

        // 2. Crear Personal (2 Cocineros + 1 Cajero por unidad = 8 cocineros + 4 cajeros)
        Set<Cocinero> cocineros = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            Cocinero cocinero = new Cocinero(
                    0,
                    "Cocinero" + i,
                    "Chef" + i,
                    30000000L + i,
                    LocalDate.of(1990, 1, i % 28 + 1),
                    LocalDate.of(2020, 1, 1),
                    i % 2 == 0 ? "Pastas" : "Carnes",
                    i % 3 + 1
            );
            int cochineroId = cocineroDao.agregar(cocinero);
            cocinero.setId(cochineroId);
            cocineros.add(cocinero);
        }

        Set<Cajero> cajeros = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            Cajero cajero = new Cajero(
                    0,
                    "Cajero" + i,
                    "Caja" + i,
                    40000000L + i,
                    LocalDate.of(1992, 1, i % 28 + 1),
                    LocalDate.of(2019, 1, 1),
                    i % 2 == 0 ? "Mañana" : "Noche"
            );
            int cajeroId = cajeroDao.agregar(cajero);
            cajero.setId(cajeroId);
            cajeros.add(cajero);
        }

        // 3. Crear 2 FoodTrucks
        FoodTruck foodTruck1 = new FoodTruck();
        foodTruck1.setNombreComercial("FoodTruck Express");
        foodTruck1.setSuperficie(15.5f);
        foodTruck1.setCodigo("FT0001ABCD");
        foodTruck1.setResponsableACargo(cocineros.iterator().next());
        foodTruck1.setPatente("ABC123");
        foodTruck1.setRequiereConexion(true);

        Cocinero[] cocinerosArray = cocineros.toArray(new Cocinero[0]);
        Cajero[] acerosArray = cajeros.toArray(new Cajero[0]);

        foodTruck1.agregarPersonal(cocinerosArray[0]);
        foodTruck1.agregarPersonal(cocinerosArray[1]);
        foodTruck1.agregarPersonal(cocinerosArray[2]);
        foodTruck1.agregarPersonal(acerosArray[0]);

        int ft1Id = foodTruckDao.agregar(foodTruck1);
        foodTruck1.setId(ft1Id);
        assertTrue(ft1Id > 0, "FoodTruck1 debe guardarse");

        FoodTruck foodTruck2 = new FoodTruck();
        foodTruck2.setNombreComercial("FoodTruck Deluxe");
        foodTruck2.setSuperficie(18.0f);
        foodTruck2.setCodigo("FT0002EFGH");
        foodTruck2.setResponsableACargo(cocinerosArray[3]);
        foodTruck2.setPatente("XYZ789");
        foodTruck2.setRequiereConexion(false);
        foodTruck2.agregarPersonal(cocinerosArray[3]);
        foodTruck2.agregarPersonal(cocinerosArray[4]);
        foodTruck2.agregarPersonal(cocinerosArray[5]);
        foodTruck2.agregarPersonal(acerosArray[1]);

        int ft2Id = foodTruckDao.agregar(foodTruck2);
        foodTruck2.setId(ft2Id);
        assertTrue(ft2Id > 0, "FoodTruck2 debe guardarse");

        // 4. Crear 2 Puestos Desarmables
        PuestoDesarmable puesto1 = new PuestoDesarmable();
        puesto1.setNombreComercial("Puesto Gourmet 1");
        puesto1.setSuperficie(25.0f);
        puesto1.setCodigo("PD0001IJKL");
        puesto1.setResponsableACargo(cocinerosArray[6]);
        puesto1.setCantidadCarpas(3);
        puesto1.setTiempoMontajeMinutos(120.0f);
        puesto1.agregarPersonal(cocinerosArray[6]);
        puesto1.agregarPersonal(cocinerosArray[7]);
        puesto1.agregarPersonal(cocinerosArray[0]);
        puesto1.agregarPersonal(acerosArray[2]);

        int pd1Id = puestoDao.agregar(puesto1);
        puesto1.setId(pd1Id);
        assertTrue(pd1Id > 0, "PuestoDesarmable1 debe guardarse");

        PuestoDesarmable puesto2 = new PuestoDesarmable();
        puesto2.setNombreComercial("Puesto Gourmet 2");
        puesto2.setSuperficie(20.0f);
        puesto2.setCodigo("PD0002MNOP");
        puesto2.setResponsableACargo(cocinerosArray[1]);
        puesto2.setCantidadCarpas(2);
        puesto2.setTiempoMontajeMinutos(90.0f);
        puesto2.agregarPersonal(cocinerosArray[1]);
        puesto2.agregarPersonal(cocinerosArray[2]);
        puesto2.agregarPersonal(cocinerosArray[3]);
        puesto2.agregarPersonal(acerosArray[3]);

        int pd2Id = puestoDao.agregar(puesto2);
        puesto2.setId(pd2Id);
        assertTrue(pd2Id > 0, "PuestoDesarmable2 debe guardarse");

        // 5. Crear 3 Platos para Puesto1
        Plato plato1_1 = new Plato("Milanesa", 150.0f, 60.0f);
        int plato1_1Id = platoDao.agregar(plato1_1);
        plato1_1.setId(plato1_1Id);
        puesto1.agregarPlato(plato1_1);

        Plato plato1_2 = new Plato("Pasta Carbonara", 180.0f, 70.0f);
        int plato1_2Id = platoDao.agregar(plato1_2);
        plato1_2.setId(plato1_2Id);
        puesto1.agregarPlato(plato1_2);

        Plato plato1_3 = new Plato("Bife Mariposa", 220.0f, 90.0f);
        int plato1_3Id = platoDao.agregar(plato1_3);
        plato1_3.setId(plato1_3Id);
        puesto1.agregarPlato(plato1_3);

        // 6. Crear 3 Platos para Puesto2
        Plato plato2_1 = new Plato("Pizza Margherita", 160.0f, 55.0f);
        int plato2_1Id = platoDao.agregar(plato2_1);
        plato2_1.setId(plato2_1Id);
        puesto2.agregarPlato(plato2_1);

        Plato plato2_2 = new Plato("Empanadas", 100.0f, 35.0f);
        int plato2_2Id = platoDao.agregar(plato2_2);
        plato2_2.setId(plato2_2Id);
        puesto2.agregarPlato(plato2_2);

        Plato plato2_3 = new Plato("Sándwich Chorizo", 120.0f, 45.0f);
        int plato2_3Id = platoDao.agregar(plato2_3);
        plato2_3.setId(plato2_3Id);
        puesto2.agregarPlato(plato2_3);

        puestoDao.actualizar(puesto1);
        puestoDao.actualizar(puesto2);

        // 7. Crear Pedidos para cada unidad

        // Pedido 1 - FoodTruck1
        Pedido pedido1 = new Pedido();
        pedido1.setFecha(LocalDate.of(2024, 12, 10));
        pedido1.setFestival(festival);
        pedido1.setUnidad(foodTruck1);

        int pedido1Id = pedidoDao.agregar(pedido1);
        pedido1.setId(pedido1Id);
        assertTrue(pedido1Id > 0, "Pedido1 debe guardarse");

        DetallePedido detalle1_1 = new DetallePedido(pedido1, plato1_1, 5);
        detalleDao.agregar(detalle1_1);

        // Pedido 2 - FoodTruck2
        Pedido pedido2 = new Pedido();
        pedido2.setFecha(LocalDate.of(2024, 12, 11));
        pedido2.setFestival(festival);
        pedido2.setUnidad(foodTruck2);

        int pedido2Id = pedidoDao.agregar(pedido2);
        pedido2.setId(pedido2Id);
        assertTrue(pedido2Id > 0, "Pedido2 debe guardarse");

        DetallePedido detalle2_1 = new DetallePedido(pedido2, plato1_2, 3);
        detalleDao.agregar(detalle2_1);

        // Pedido 3 - Puesto1
        Pedido pedido3 = new Pedido();
        pedido3.setFecha(LocalDate.of(2024, 12, 12));
        pedido3.setFestival(festival);
        pedido3.setUnidad(puesto1);

        int pedido3Id = pedidoDao.agregar(pedido3);
        pedido3.setId(pedido3Id);
        assertTrue(pedido3Id > 0, "Pedido3 debe guardarse");

        DetallePedido detalle3_1 = new DetallePedido(pedido3, plato1_1, 10);
        detalleDao.agregar(detalle3_1);

        DetallePedido detalle3_2 = new DetallePedido(pedido3, plato1_2, 8);
        detalleDao.agregar(detalle3_2);

        DetallePedido detalle3_3 = new DetallePedido(pedido3, plato1_3, 5);
        detalleDao.agregar(detalle3_3);

        // Pedido 4 - Puesto2
        Pedido pedido4 = new Pedido();
        pedido4.setFecha(LocalDate.of(2024, 12, 13));
        pedido4.setFestival(festival);
        pedido4.setUnidad(puesto2);

        int pedido4Id = pedidoDao.agregar(pedido4);
        pedido4.setId(pedido4Id);
        assertTrue(pedido4Id > 0, "Pedido4 debe guardarse");

        DetallePedido detalle4_1 = new DetallePedido(pedido4, plato2_1, 12);
        detalleDao.agregar(detalle4_1);

        DetallePedido detalle4_2 = new DetallePedido(pedido4, plato2_2, 20);
        detalleDao.agregar(detalle4_2);

        DetallePedido detalle4_3 = new DetallePedido(pedido4, plato2_3, 7);
        detalleDao.agregar(detalle4_3);

        // 8. Verificaciones
        Festival festivalRecuperado = festivalDao.traer(festivalId);
        assertNotNull(festivalRecuperado, "Festival debe recuperarse");
        assertEquals("Festival Gourmet 2024", festivalRecuperado.getNombre());
        assertEquals(100.0, festivalRecuperado.getCostoSuperficie());
        assertEquals(50.0, festivalRecuperado.getCostoMontaje());
        assertEquals(200.0, festivalRecuperado.getPlusElectricidad());
        assertEquals(3000.0, festivalRecuperado.getSueldoBase());

        FoodTruck ft1Recuperado = foodTruckDao.traer(ft1Id);
        assertNotNull(ft1Recuperado, "FoodTruck1 debe recuperarse");
        assertEquals("FT0001ABCD", ft1Recuperado.getCodigo());

        PuestoDesarmable pd1Recuperado = puestoDao.traer(pd1Id);
        assertNotNull(pd1Recuperado, "PuestoDesarmable1 debe recuperarse");
        assertEquals(3, pd1Recuperado.getCantidadCarpas());

        Plato platoRecuperado = platoDao.traer(plato1_1Id);
        assertNotNull(platoRecuperado, "Plato debe recuperarse");
        assertEquals("Milanesa", platoRecuperado.getNombre());

        Set<Pedido> pedidosFestival = pedidoDao.traerPorFestival(festivalId);
        assertEquals(4, pedidosFestival.size(), "Debe haber 4 pedidos en el festival");

        Set<Personal> todoPersonal = new HashSet<>(personalDao.traer());

        System.out.println("\n========== VERIFICACIÓN DE DATOS GUARDADOS ==========");
        System.out.println("\n📌 FESTIVAL:");
        System.out.println("  Nombre: " + festivalRecuperado.getNombre());
        System.out.println("  Temporada: " + festivalRecuperado.getTemporada());
        System.out.println("  Fechas: " + festivalRecuperado.getFechaInicio() + " a " + festivalRecuperado.getFechaFin());
        System.out.println("  Costos - Superficie: $" + festivalRecuperado.getCostoSuperficie()
                + ", Montaje: $" + festivalRecuperado.getCostoMontaje()
                + ", Electricidad: $" + festivalRecuperado.getPlusElectricidad()
                + ", Sueldo Base: $" + festivalRecuperado.getSueldoBase());

        System.out.println("\n🏪 UNIDADES DE VENTA:");
        System.out.println("  FoodTruck 1: " + ft1Recuperado.getNombreComercial() + " (Código: " + ft1Recuperado.getCodigo() + ")");
        System.out.println("    Patente: " + ft1Recuperado.getPatente());
        System.out.println("    Requiere electricidad: " + ft1Recuperado.getRequiereConexion());

        System.out.println("  FoodTruck 2: " + foodTruck2.getNombreComercial() + " (Código: " + foodTruck2.getCodigo() + ")");
        System.out.println("    Patente: " + foodTruck2.getPatente());
        System.out.println("    Requiere electricidad: " + foodTruck2.getRequiereConexion());

        System.out.println("  Puesto Desarmable 1: " + pd1Recuperado.getNombreComercial() + " (Código: " + pd1Recuperado.getCodigo() + ")");
        System.out.println("    Carpas: " + pd1Recuperado.getCantidadCarpas() + ", Tiempo montaje: " + pd1Recuperado.getTiempoMontajeMinutos() + " min");

        System.out.println("  Puesto Desarmable 2: " + puesto2.getNombreComercial() + " (Código: " + puesto2.getCodigo() + ")");
        System.out.println("    Carpas: " + puesto2.getCantidadCarpas() + ", Tiempo montaje: " + puesto2.getTiempoMontajeMinutos() + " min");

        System.out.println("\n👥 PERSONAL (" + todoPersonal.size() + " total):");
        int cocineroCount = 0, cajeroCount = 0;
        for (Personal p : todoPersonal) {
            String rol = "Desconocido";
            if (p instanceof Cocinero) {
                rol = "Cocinero";
                cocineroCount++;
            } else if (p instanceof Cajero) {
                rol = "Cajero";
                cajeroCount++;
            }
            System.out.println("  • " + p.getNombre() + " " + p.getApellido() + " (DNI: " + p.getDni() + ") - " + rol);
        }
        System.out.println("  Resumen: " + cocineroCount + " cocineros, " + cajeroCount + " cajeros");

        System.out.println("\n📋 PEDIDOS/FACTURAS (" + pedidosFestival.size() + " total):");
        for (Pedido p : pedidosFestival) {
            System.out.println("  Pedido #" + p.getId() + " - " + p.getFecha() +
                    " (Unidad: " + p.getUnidad().getNombreComercial() + ")");
            System.out.println("    Detalles: " + p.getDetalles().size() + " items");
            for (DetallePedido d : p.getDetalles()) {
                System.out.println("      • " + d.getPlato().getNombre() + " x" + d.getCantidad());
            }
        }

        System.out.println("\n========== FIN VERIFICACIÓN ==========\n");
    }

    // No cerramos el SessionFactory aqui porque es un singleton compartido entre todos los tests
}