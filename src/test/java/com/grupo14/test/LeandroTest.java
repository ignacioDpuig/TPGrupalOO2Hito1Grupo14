package com.grupo14.test;

import com.grupo14.dao.*;
import com.grupo14.datos.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LeandroTest {

    private static FestivalDao         festivalDao;
    private static CocineroDao         cocineroDao;
    private static FoodTruckDao        foodTruckDao;
    private static PuestoDesarmableDao puestoDao;

    @BeforeAll
    public static void setUp() {
        festivalDao  = new FestivalDao();
        cocineroDao  = new CocineroDao();
        foodTruckDao = new FoodTruckDao();
        puestoDao    = new PuestoDesarmableDao();
    }

    @Test
    public void cuCalcularCanonPorUnidad() throws Exception {
        System.out.println("\n========== CU LEANDRO: Canon por unidad de venta en un festival ==========\n");

        // =====================================================================
        // PASO 1: INSERCION DE DATOS EN LA BD
        // =====================================================================

        Festival festival = new Festival();
        festival.setNombre("Festival Leandro 2024");
        festival.setTemporada("Verano");
        festival.setFechaInicio(LocalDate.of(2024, 1, 10));
        festival.setFechaFin(LocalDate.of(2024, 1, 20));
        festival.setCostoSuperficie(120.0);  // $120 por m2
        festival.setCostoMontaje(30.0);      // $30 por minuto de montaje
        festival.setPlusElectricidad(500.0); // $500 fijo si la unidad requiere conexion electrica
        festival.setSueldoBase(3000.0);
        festival.setId(festivalDao.agregar(festival)); // INSERT en tabla festival
        assertTrue(festival.getId() > 0, "Festival debe guardarse en la BD");

        Cocinero responsable = new Cocinero(0, "Marcos", "Lima", 35500001L,
                LocalDate.of(1986, 3, 14), LocalDate.of(2018, 7, 1),
                "Italiana", 2);
        responsable.setId(cocineroDao.agregar(responsable)); // INSERT en personal + cocinero


        FoodTruck ft1 = new FoodTruck();
        ft1.setNombreComercial("Burger Bus");
        ft1.setSuperficie(20.0f);
        ft1.setCodigo("LEAN000001");
        ft1.setResponsableACargo(responsable);
        ft1.setPatente("LEA001");
        ft1.setRequiereConexion(true); // paga plus de electricidad
        ft1.setId(foodTruckDao.agregar(ft1)); // INSERT en unidad_venta + food_truck


        FoodTruck ft2 = new FoodTruck();
        ft2.setNombreComercial("Taco Truck");
        ft2.setSuperficie(15.0f);
        ft2.setCodigo("LEAN000002");
        ft2.setResponsableACargo(responsable);
        ft2.setPatente("LEA002");
        ft2.setRequiereConexion(false); // no paga plus de electricidad
        ft2.setId(foodTruckDao.agregar(ft2)); // INSERT en unidad_venta + food_truck


        PuestoDesarmable puesto1 = new PuestoDesarmable();
        puesto1.setNombreComercial("Pizzeria del Sur");
        puesto1.setSuperficie(25.0f);
        puesto1.setCodigo("LEAN000003");
        puesto1.setResponsableACargo(responsable);
        puesto1.setCantidadCarpas(3);
        puesto1.setTiempoMontajeMinutos(120.0f);
        puesto1.setId(puestoDao.agregar(puesto1)); // INSERT en unidad_venta + puesto_desarmable


        PuestoDesarmable puesto2 = new PuestoDesarmable();
        puesto2.setNombreComercial("Empanadas Express");
        puesto2.setSuperficie(10.0f);
        puesto2.setCodigo("LEAN000004");
        puesto2.setResponsableACargo(responsable);
        puesto2.setCantidadCarpas(1);
        puesto2.setTiempoMontajeMinutos(45.0f);
        puesto2.setId(puestoDao.agregar(puesto2)); // INSERT en unidad_venta + puesto_desarmable
        

        Festival  festivalRecuperado = festivalDao.traer(festival.getId()); //Firma de traer. pasar el festival completo para respetar el paradigma de objeto verificar que en la lcase trabajo con festival
        FoodTruck ft1Recuperado      = foodTruckDao.traer(ft1.getId());
        FoodTruck ft2Recuperado      = foodTruckDao.traer(ft2.getId());
        PuestoDesarmable pd1Rec      = puestoDao.traer(puesto1.getId());
        PuestoDesarmable pd2Rec      = puestoDao.traer(puesto2.getId());

        assertNotNull(festivalRecuperado, "Festival debe recuperarse de la BD");
        assertNotNull(ft1Recuperado,      "FoodTruck 1 debe recuperarse de la BD");
        assertNotNull(ft2Recuperado,      "FoodTruck 2 debe recuperarse de la BD");
        assertNotNull(pd1Rec,             "Puesto 1 debe recuperarse de la BD");
        assertNotNull(pd2Rec,             "Puesto 2 debe recuperarse de la BD");


        System.out.println("Festival: " + festivalRecuperado.getNombre());
        System.out.printf("Costos: superficie=$%.2f/m2 | montaje=$%.2f/min | electricidad=$%.2f%n%n",
                festivalRecuperado.getCostoSuperficie(),
                festivalRecuperado.getCostoMontaje(),
                festivalRecuperado.getPlusElectricidad());

        double canonTotal = 0;

        System.out.println("  --- Food Trucks ---");
        for (FoodTruck ft : new FoodTruck[]{ft1Recuperado, ft2Recuperado}) {
            double canon = ft.calcularCannon(festivalRecuperado); // POLIMORFISMO
            canonTotal += canon;
            System.out.printf("  [FoodTruck] %s | Superficie: %.1fm2 | Electricidad: %s | Canon: $%.2f%n",
                    ft.getNombreComercial(),
                    ft.getSuperficie(),
                    ft.getRequiereConexion() ? "Si" : "No",
                    canon);
            assertTrue(canon > 0, "Canon del FoodTruck debe ser mayor a 0");
        }

        System.out.println("  --- Puestos Desarmables ---");
        for (PuestoDesarmable pd : new PuestoDesarmable[]{pd1Rec, pd2Rec}) {
            double canon = pd.calcularCannon(festivalRecuperado); // POLIMORFISMO
            canonTotal += canon;
            System.out.printf("  [PuestoDesarmable] %s | Superficie: %.1fm2 | Montaje: %.0fmin | Canon: $%.2f%n",
                    pd.getNombreComercial(),
                    pd.getSuperficie(),
                    pd.getTiempoMontajeMinutos(),
                    canon);
            assertTrue(canon > 0, "Canon del Puesto debe ser mayor a 0");
        }

        System.out.printf("%n  Canon total del festival: $%.2f%n", canonTotal);
        assertTrue(canonTotal > 0, "Canon total debe ser mayor a 0");

        System.out.println("\n========== FIN CU LEANDRO ==========\n");
    }

}
