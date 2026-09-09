package com.grupo14.test.dao;

import com.grupo14.dao.*;
import com.grupo14.datos.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caso de Uso - Responsable: Joaquin
 *
 * CU: Dado un festival, listar todos sus pedidos con los detalles de cada uno,
 *     distinguiendo si la unidad de venta es un FoodTruck o un PuestoDesarmable.
 *
 * Este test crea DOS festivales para demostrar que las consultas filtran
 * correctamente por festival (y no mezclan datos de otro festival).
 *
 * Queries de los DAOs que ejercita (ademas de los INSERT/get simples):
 *  - PedidoDao.traerPorFestival(idFestival) : HQL filtrando por la relacion Pedido->Festival.
 *  - PedidoDao.traerPorUnidad(idUnidad)     : HQL filtrando por la relacion Pedido->UnidadVenta.
 *  - FoodTruckDao.traerConConexion()        : HQL filtrando por el atributo booleano requiereConexion.
 */
public class JoaquinTest {

    private static FestivalDao festivalDao;
    private static CocineroDao cocineroDao;
    private static PlatoDao platoDao;
    private static FoodTruckDao foodTruckDao;
    private static PuestoDesarmableDao puestoDao;
    private static PedidoDao pedidoDao;
    private static DetallePedidoDao detalleDao;

    @BeforeAll
    public static void setUp() {
        festivalDao  = new FestivalDao();
        cocineroDao  = new CocineroDao();
        platoDao     = new PlatoDao();
        foodTruckDao = new FoodTruckDao();
        puestoDao    = new PuestoDesarmableDao();
        pedidoDao    = new PedidoDao();
        detalleDao   = new DetallePedidoDao();
    }

    @Test
    public void cuListarPedidosPorFestivalConDetalles() throws Exception {
        System.out.println("\n========== CU JOAQUIN: Pedidos de un festival con detalles ==========\n");

        // --- INSERCION DE DATOS ---

        // Festival
        Festival festival = new Festival();
        festival.setNombre("Festival Joaquin 2024");
        festival.setTemporada("Invierno");
        festival.setFechaInicio(LocalDate.of(2024, 7, 1));
        festival.setFechaFin(LocalDate.of(2024, 7, 15));
        festival.setCostoSuperficie(80.0);
        festival.setCostoMontaje(40.0);
        festival.setPlusElectricidad(150.0);
        festival.setSueldoBase(2800.0);
        int idFestival = festivalDao.agregar(festival);
        festival.setId(idFestival);
        assertTrue(idFestival > 0, "Festival debe guardarse");

        // Responsable
        Cocinero responsable = new Cocinero(0, "Diego", "Vargas", 33200001L,
                LocalDate.of(1985, 6, 18), LocalDate.of(2020, 2, 1),
                "Sushi", 1);
        int idResp = cocineroDao.agregar(responsable);
        responsable.setId(idResp);

        Cocinero responsable2 = new Cocinero(1, "Pablo", "Rodriguez", 3335674L,
        LocalDate.of(1989, 6, 20), LocalDate.of(2021, 2, 1),
        "Sushi", 1);
        int idResp2 = cocineroDao.agregar(responsable2);
        responsable2.setId(idResp2);

        // FoodTruck 
        FoodTruck ft = new FoodTruck();
        ft.setNombreComercial("Sushi Wheels");
        ft.setSuperficie(12.0f);
        ft.setCodigo("JOAQ000001");
        ft.setResponsableACargo(responsable);
        ft.setPatente("JOA001");
        ft.setRequiereConexion(true);
        int idFt = foodTruckDao.agregar(ft);
        ft.setId(idFt);

        // PuestoDesarmable
        PuestoDesarmable puesto = new PuestoDesarmable();
        puesto.setNombreComercial("La Parrilla de Joaquin");
        puesto.setSuperficie(22.0f);
        puesto.setCodigo("JOAQ000002");
        puesto.setResponsableACargo(responsable2);
        puesto.setCantidadCarpas(3);
        puesto.setTiempoMontajeMinutos(60.0f);
        int idPuesto = puestoDao.agregar(puesto);
        puesto.setId(idPuesto);

        // Platos
        Plato plato1 = new Plato("Rolls de Salmon", 220.0f, 90.0f);
        plato1.setId(platoDao.agregar(plato1));

        Plato plato2 = new Plato("Asado de Tira", 350.0f, 150.0f);
        plato2.setId(platoDao.agregar(plato2));

        Plato plato3 = new Plato("Chorizo al Pan", 130.0f, 50.0f);
        plato3.setId(platoDao.agregar(plato3));

        // Pedido 1 en FoodTruck 
        Pedido pedido1 = new Pedido();
        pedido1.setFecha(LocalDate.of(2024, 7, 5));
        pedido1.setFestival(festival);
        pedido1.setUnidad(ft);
        int idPedido1 = pedidoDao.agregar(pedido1);
        pedido1.setId(idPedido1);
        detalleDao.agregar(new DetallePedido(pedido1, plato1, 4));
        detalleDao.agregar(new DetallePedido(pedido1, plato2, 2));

        // Pedido 2 en PuestoDesarmable 
        Pedido pedido2 = new Pedido();
        pedido2.setFecha(LocalDate.of(2024, 7, 6));
        pedido2.setFestival(festival);
        pedido2.setUnidad(puesto);
        int idPedido2 = pedidoDao.agregar(pedido2);
        pedido2.setId(idPedido2);
        detalleDao.agregar(new DetallePedido(pedido2, plato2, 5));
        detalleDao.agregar(new DetallePedido(pedido2, plato3, 8));

        // Pedido 3 en FoodTruck
        Pedido pedido3 = new Pedido();
        pedido3.setFecha(LocalDate.of(2024, 7, 7));
        pedido3.setFestival(festival);
        pedido3.setUnidad(ft);
        int idPedido3 = pedidoDao.agregar(pedido3);
        pedido3.setId(idPedido3);
        detalleDao.agregar(new DetallePedido(pedido3, plato1, 6));

        // --- SEGUNDO FESTIVAL (para probar que las consultas filtran bien) ---
        Festival festival2 = new Festival();
        festival2.setNombre("Festival Joaquin Verano 2025");
        festival2.setTemporada("Verano");
        festival2.setFechaInicio(LocalDate.of(2025, 1, 5));
        festival2.setFechaFin(LocalDate.of(2025, 1, 12));
        festival2.setCostoSuperficie(95.0);
        festival2.setCostoMontaje(50.0);
        festival2.setPlusElectricidad(200.0);
        festival2.setSueldoBase(3200.0);
        int idFestival2 = festivalDao.agregar(festival2);
        festival2.setId(idFestival2);
        assertTrue(idFestival2 > 0, "El segundo festival debe guardarse");

        // FoodTruck del segundo festival
        FoodTruck ft2 = new FoodTruck();
        ft2.setNombreComercial("Tacos del Verano");
        ft2.setSuperficie(14.0f);
        ft2.setCodigo("JOAQ000003");
        ft2.setResponsableACargo(responsable);
        ft2.setPatente("JOA002");
        ft2.setRequiereConexion(true);
        int idFt2 = foodTruckDao.agregar(ft2);
        ft2.setId(idFt2);

        // Pedido del segundo festival
        Pedido pedidoOtro = new Pedido();
        pedidoOtro.setFecha(LocalDate.of(2025, 1, 6));
        pedidoOtro.setFestival(festival2);
        pedidoOtro.setUnidad(ft2);
        pedidoOtro.setId(pedidoDao.agregar(pedidoOtro));
        detalleDao.agregar(new DetallePedido(pedidoOtro, plato3, 10));

        // --- CONSULTA A LA BD ---

        // Traer todos los pedidos del PRIMER festival.
        // HQL: from Pedido p where p.festival.id = :idFestival
        Set<Pedido> pedidos = pedidoDao.traerPorFestival(idFestival);
        assertFalse(pedidos.isEmpty(), "Debe haber pedidos para el festival");
        assertEquals(3, pedidos.size(), "Deben ser 3 pedidos del primer festival (no debe incluir el del segundo)");

        // El segundo festival debe tener exactamente 1 pedido (filtrado correcto)
        Set<Pedido> pedidosFestival2 = pedidoDao.traerPorFestival(idFestival2);
        assertEquals(1, pedidosFestival2.size(), "El segundo festival debe tener 1 solo pedido");

        System.out.println("Festival: " + festival.getNombre());
        System.out.println("Total pedidos: " + pedidos.size());

        double recaudacionTotal = 0;
        for (Pedido p : pedidos) {
            // Distinguir tipo de unidad usando herencia 
            String tipoUnidad = (p.getUnidad() instanceof FoodTruck)
                    ? "FoodTruck"
                    : (p.getUnidad() instanceof PuestoDesarmable)
                    ? "PuestoDesarmable"
                    : "UnidadVenta";

            System.out.println("\n  Pedido #" + p.getId()
                    + " | Fecha: " + p.getFecha()
                    + " | Unidad: " + p.getUnidad().getNombreComercial()
                    + " [" + tipoUnidad + "]");

            // Detalles del pedido 
            for (DetallePedido d : p.getDetalles()) {
                double subtotal = d.getPlato().getPrecioVenta() * d.getCantidad();
                recaudacionTotal += subtotal;
                System.out.println("    - " + d.getPlato().getNombre()
                        + " x" + d.getCantidad()
                        + " = $" + String.format("%.2f", subtotal));
            }
        }

        System.out.println("\n  Recaudacion total del festival: $" + String.format("%.2f", recaudacionTotal));
        assertTrue(recaudacionTotal > 0, "La recaudacion debe ser mayor a 0");

        // --- CONSULTA EXTRA 1: 
        Set<Pedido> pedidosDelFoodTruck = pedidoDao.traerPorUnidad(idFt);
        assertEquals(2, pedidosDelFoodTruck.size(),
                "El FoodTruck 'Sushi Wheels' debe tener 2 pedidos");
        Set<Pedido> pedidosDelPuesto = pedidoDao.traerPorUnidad(idPuesto);
        assertEquals(1, pedidosDelPuesto.size(),
                "El puesto 'La Parrilla de Joaquin' debe tener 1 pedido");

        System.out.println("\n  Pedidos por unidad:");
        System.out.println("    - " + ft.getNombreComercial() + ": " + pedidosDelFoodTruck.size() + " pedidos");
        System.out.println("    - " + puesto.getNombreComercial() + ": " + pedidosDelPuesto.size() + " pedidos");

        Set<FoodTruck> conConexion = foodTruckDao.traerConConexion();
        assertTrue(conConexion.size() >= 2,
                "Deben aparecer al menos los 2 food trucks con conexion creados en el test");
        assertTrue(conConexion.stream().allMatch(FoodTruck::getRequiereConexion),
                "Todos los food trucks devueltos deben requerir conexion");

        System.out.println("\n  Food trucks con conexion electrica: " + conConexion.size());
        for (FoodTruck f : conConexion) {
            System.out.println("    - " + f.getNombreComercial() + " (patente " + f.getPatente() + ")");
        }

        System.out.println("\n========== FIN CU JOAQUIN ==========\n");
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }
}
