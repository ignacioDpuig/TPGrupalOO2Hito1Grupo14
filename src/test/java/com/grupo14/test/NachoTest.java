package com.grupo14.test.dao;

import com.grupo14.dao.*;
import com.grupo14.datos.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TEST DE INTEGRACION - Responsable: Ignacio Puig
 *
 * CU: Dar de alta un Festival completo y ASIGNARLE sus unidades de venta
 *     (un FoodTruck y un PuestoDesarmable), cada una con su staff, sus platos,
 *     y sus pedidos con detalles. Luego recuperar todo desde la BD y verificar
 *     que las relaciones quedaron correctamente persistidas.
 *
 * Conceptos de OO / persistencia que demuestra:
 *  - HERENCIA: Personal -> Cajero / Cocinero.  UnidadVenta -> FoodTruck / PuestoDesarmable.
 *  - RELACION Festival 1..* UnidadVenta: se asignan las unidades al festival y se
 *              persiste la FK id_festival (lado dueño en UnidadVenta).
 *  - RELACION UnidadVenta *..* Personal (staff) y *..* Plato (menu).
 *  - RELACION Festival/Unidad 1..* Pedido 1..* DetallePedido.
 *  - POLIMORFISMO: calcularCannon() distinto en cada subclase de UnidadVenta.
 *
 * Flujo del test (integracion de punta a punta):
 *  1. INSERT: festival, personal (cocineros/cajeros), unidades, platos, pedidos y detalles.
 *  2. ASIGNACION: se agregan las unidades al festival (relacion directa).
 *  3. SELECT: se recupera el festival desde la BD con sus unidades.
 *  4. VERIFICACION: se comprueban las relaciones y los calculos de negocio.
 */
public class NachoTest {

    // DAOs: capa de acceso a datos, uno por entidad
    private static FestivalDao         festivalDao;
    private static FoodTruckDao        foodTruckDao;
    private static PuestoDesarmableDao puestoDao;
    private static CocineroDao         cocineroDao;
    private static CajeroDao           cajeroDao;
    private static PlatoDao            platoDao;
    private static PedidoDao           pedidoDao;
    private static DetallePedidoDao    detalleDao;

    @BeforeAll
    public static void setUp() {
        festivalDao  = new FestivalDao();
        foodTruckDao = new FoodTruckDao();
        puestoDao    = new PuestoDesarmableDao();
        cocineroDao  = new CocineroDao();
        cajeroDao    = new CajeroDao();
        platoDao     = new PlatoDao();
        pedidoDao    = new PedidoDao();
        detalleDao   = new DetallePedidoDao();
    }

    @Test
    public void cuAltaFestivalConUnidadesAsignadas() throws Exception {
        System.out.println("\n========== CU NACHO (INTEGRACION): Festival con unidades asignadas ==========\n");

        
        Festival festival = new Festival();
        festival.setNombre("Epicentro Gourmet Nacho 2024");
        festival.setTemporada("Primavera");
        festival.setFechaInicio(LocalDate.of(2024, 9, 10));
        festival.setFechaFin(LocalDate.of(2024, 9, 20));
        festival.setCostoSuperficie(100.0); // $100 por m2
        festival.setCostoMontaje(25.0);     // $25 por minuto de montaje
        festival.setPlusElectricidad(400.0);// $400 fijo si la unidad requiere electricidad
        festival.setSueldoBase(3000.0);
        festival.setId(festivalDao.agregar(festival)); // INSERT en tabla festival
        assertTrue(festival.getId() > 0, "El festival debe guardarse en la BD");

        
        Cocinero chefResponsable = new Cocinero(0, "Ana", "Torres", 32100001L,
                LocalDate.of(1988, 4, 12), LocalDate.of(2019, 3, 1), "Pastas", 3);
        chefResponsable.setId(cocineroDao.agregar(chefResponsable));

        Cocinero cocineroParrilla = new Cocinero(0, "Bruno", "Sosa", 32100002L,
                LocalDate.of(1990, 7, 22), LocalDate.of(2021, 5, 10), "Parrilla", 2);
        cocineroParrilla.setId(cocineroDao.agregar(cocineroParrilla));

        Cajero cajeroManiana = new Cajero(0, "Clara", "Ruiz", 32100003L,
                LocalDate.of(1995, 2, 5), LocalDate.of(2022, 1, 15), "Mañana");
        cajeroManiana.setId(cajeroDao.agregar(cajeroManiana));

        Cajero cajeroTarde = new Cajero(0, "Diego", "Paz", 32100004L,
                LocalDate.of(1993, 11, 30), LocalDate.of(2020, 8, 3), "Tarde");
        cajeroTarde.setId(cajeroDao.agregar(cajeroTarde));

        Plato ravioles   = new Plato("Ravioles de la casa", 300.0f, 120.0f);
        ravioles.setId(platoDao.agregar(ravioles));

        Plato asadoTira  = new Plato("Asado de tira", 450.0f, 200.0f);
        asadoTira.setId(platoDao.agregar(asadoTira));

        Plato empanadas  = new Plato("Empanadas x6", 180.0f, 70.0f);
        empanadas.setId(platoDao.agregar(empanadas));

        
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setNombreComercial("El Rincón Criollo");
        foodTruck.setSuperficie(18.0f);
        foodTruck.setCodigo("NACHO00001");        // 10 caracteres (validacion)
        foodTruck.setResponsableACargo(chefResponsable);
        foodTruck.setPatente("NAC001");
        foodTruck.setRequiereConexion(true);       // paga plus electricidad
        foodTruck.agregarPersonal(chefResponsable);
        foodTruck.agregarPersonal(cajeroManiana);
        foodTruck.agregarPlato(ravioles);
        foodTruck.agregarPlato(empanadas);
        festival.agregarUnidad(foodTruck);          // ASIGNA la unidad al festival (setea festival)
        foodTruck.setId(foodTruckDao.agregar(foodTruck)); // INSERT: unidad_venta (con id_festival), food_truck, intermedias
        assertTrue(foodTruck.getId() > 0, "El FoodTruck debe guardarse en la BD");

        PuestoDesarmable puesto = new PuestoDesarmable();
        puesto.setNombreComercial("La Parrilla del Sur");
        puesto.setSuperficie(30.0f);
        puesto.setCodigo("NACHO00002");
        puesto.setResponsableACargo(cocineroParrilla);
        puesto.setCantidadCarpas(2);
        puesto.setTiempoMontajeMinutos(90.0f);
        puesto.agregarPersonal(cocineroParrilla);
        puesto.agregarPersonal(cajeroTarde);
        puesto.agregarPlato(asadoTira);
        puesto.agregarPlato(empanadas);
        festival.agregarUnidad(puesto);             // ASIGNA la unidad al festival
        puesto.setId(puestoDao.agregar(puesto));    // INSERT: unidad_venta (con id_festival), puesto_desarmable, intermedias
        assertTrue(puesto.getId() > 0, "El Puesto debe guardarse en la BD");

       
        Pedido pedidoFt = new Pedido();
        pedidoFt.setFecha(LocalDate.of(2024, 9, 12));
        pedidoFt.setFestival(festival);
        pedidoFt.setUnidad(foodTruck);
        pedidoFt.setId(pedidoDao.agregar(pedidoFt));
        detalleDao.agregar(new DetallePedido(pedidoFt, ravioles, 5));
        detalleDao.agregar(new DetallePedido(pedidoFt, empanadas, 3));

        Pedido pedidoPuesto = new Pedido();
        pedidoPuesto.setFecha(LocalDate.of(2024, 9, 13));
        pedidoPuesto.setFestival(festival);
        pedidoPuesto.setUnidad(puesto);
        pedidoPuesto.setId(pedidoDao.agregar(pedidoPuesto));
        detalleDao.agregar(new DetallePedido(pedidoPuesto, asadoTira, 4));
        detalleDao.agregar(new DetallePedido(pedidoPuesto, empanadas, 6));

        // =====================================================================
        // PASO 7: SELECT - recuperamos el festival desde la BD con sus unidades
        // (el set 'unidades' es lazy=false, se carga junto con el festival)
        // =====================================================================
        Festival festivalBd = festivalDao.traer(festival.getId());
        assertNotNull(festivalBd, "El festival debe recuperarse de la BD");

        Set<UnidadVenta> unidades = festivalBd.getUnidades();
        assertNotNull(unidades, "El festival debe tener su set de unidades");
        assertEquals(2, unidades.size(), "El festival debe tener 2 unidades asignadas");

        // Contamos por tipo usando herencia/polimorfismo
        long cantFoodTrucks = unidades.stream().filter(u -> u instanceof FoodTruck).count();
        long cantPuestos    = unidades.stream().filter(u -> u instanceof PuestoDesarmable).count();
        assertEquals(1, cantFoodTrucks, "Debe haber 1 FoodTruck asignado");
        assertEquals(1, cantPuestos,    "Debe haber 1 PuestoDesarmable asignado");

        // =====================================================================
        // PASO 8: VERIFICACION de la relacion inversa (unidad -> festival)
        // y del resto de relaciones cargadas desde la BD
        // =====================================================================
        System.out.println("Festival: " + festivalBd.getNombre() + " (id=" + festivalBd.getId() + ")");
        System.out.println("Unidades asignadas: " + unidades.size());

        double canonTotal = 0;
        for (UnidadVenta u : unidades) {
            // La unidad recuperada debe apuntar de vuelta a SU festival (FK id_festival)
            assertNotNull(u.getFestival(), "Cada unidad debe conocer su festival");
            assertEquals(festivalBd.getId(), u.getFestival().getId(),
                    "La unidad debe estar asignada al festival correcto");

            // staff y platos deben haberse persistido (tablas intermedias)
            assertFalse(u.getStaff().isEmpty(), "La unidad debe tener staff cargado");
            assertFalse(u.getPlatos().isEmpty(), "La unidad debe tener platos cargados");

            String tipo = (u instanceof FoodTruck) ? "FoodTruck" : "PuestoDesarmable";
            double canon = u.calcularCannon(festivalBd); // POLIMORFISMO
            double sueldos = u.calcularSueldos(festivalBd);
            canonTotal += canon;

            System.out.printf("%n  [%s] %s | superficie=%.1fm2 | staff=%d | platos=%d%n",
                    tipo, u.getNombreComercial(), u.getSuperficie(),
                    u.getStaff().size(), u.getPlatos().size());
            System.out.printf("     canon=$%.2f | sueldos=$%.2f%n", canon, sueldos);

            assertTrue(canon > 0, "El canon de la unidad debe ser mayor a 0");

            // Mostrar staff distinguiendo rol (herencia Personal)
            for (Personal p : u.getStaff()) {
                String rol = (p instanceof Cocinero)
                        ? "Cocinero[" + ((Cocinero) p).getEspecialidad() + "]"
                        : (p instanceof Cajero)
                        ? "Cajero[" + ((Cajero) p).getTurno() + "]"
                        : "Personal";
                System.out.println("       - " + p.getNombre() + " " + p.getApellido() + " | " + rol);
            }
        }

        // =====================================================================
        // PASO 9: VERIFICACION de pedidos por festival y recaudacion
        // =====================================================================
        Set<Pedido> pedidos = pedidoDao.traerPorFestival(festivalBd.getId());
        assertEquals(2, pedidos.size(), "El festival debe tener 2 pedidos");

        double recaudacion = 0;
        int totalDetalles = 0;
        for (Pedido p : pedidos) {
            for (DetallePedido d : p.getDetalles()) {
                recaudacion += d.getPlato().getPrecioVenta() * d.getCantidad();
                totalDetalles++;
            }
        }
        assertEquals(4, totalDetalles, "Deben existir 4 lineas de detalle en total");
        assertTrue(recaudacion > 0, "La recaudacion debe ser mayor a 0");

        System.out.printf("%n  Canon total del festival : $%.2f%n", canonTotal);
        System.out.printf("  Recaudacion total        : $%.2f%n", recaudacion);
        System.out.printf("  Pedidos                  : %d%n", pedidos.size());

        System.out.println("\n========== FIN CU NACHO (INTEGRACION) ==========\n");
    }

    // El SessionFactory es un singleton compartido; no se cierra aca.
}
