package com.grupo14.test.dao;

import com.grupo14.dao.*;
import com.grupo14.datos.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CASO DE USO - Responsable: Ignacio Puig
 *
 * CU: Dado un FoodTruck, listar su staff distinguiendo Cajeros de Cocineros.
 *
 * Conceptos de OO demostrados:
 *  - HERENCIA: Personal es la clase padre. Cajero y Cocinero son subclases con
 *              atributos propios (turno / especialidad + categoria).
 *  - UNO A MUCHOS: Una UnidadVenta tiene muchos Personal en su staff.
 *
 * Flujo del test:
 *  1. Inserta datos de prueba en la BD (Cocineros, Cajero, FoodTruck con staff).
 *  2. Recupera el FoodTruck desde la BD con su staff cargado.
 *  3. Filtra el staff por tipo usando instanceof (polimorfismo de herencia).
 *  4. Imprime el resultado y verifica con assertions.
 */
public class NachoTest {

    // DAOs: capa de acceso a datos, uno por entidad
    private static FoodTruckDao foodTruckDao;
    private static CocineroDao  cocineroDao;
    private static CajeroDao    cajeroDao;
    private static PersonalDao  personalDao;

    /**
     * Se ejecuta UNA VEZ antes de todos los tests de la clase.
     * Instancia los DAOs que se van a usar.
     */
    @BeforeAll
    public static void setUp() {
        foodTruckDao = new FoodTruckDao();
        cocineroDao  = new CocineroDao();
        cajeroDao    = new CajeroDao();
        personalDao  = new PersonalDao();
    }

    @Test
    public void cuListarStaffPorRol() throws Exception {
        System.out.println("\n========== CU NACHO: Listar staff de una unidad por rol ==========\n");

        // =====================================================================
        // PASO 1: INSERCION DE DATOS EN LA BD
        // =====================================================================

        // Cocinero 1 — subclase de Personal con especialidad "Pastas" y categoria 2
        Cocinero cocinero1 = new Cocinero(0, "Ana", "Torres", 32100001L,
                LocalDate.of(1988, 4, 12), LocalDate.of(2021, 3, 1),
                "Pastas", 2);
        cocinero1.setId(cocineroDao.agregar(cocinero1)); // INSERT en tablas personal + cocinero

        // Cocinero 2 — subclase de Personal con especialidad "Carnes" y categoria 3
        Cocinero cocinero2 = new Cocinero(0, "Bruno", "Sosa", 32100002L,
                LocalDate.of(1990, 7, 22), LocalDate.of(2022, 5, 10),
                "Carnes", 3);
        cocinero2.setId(cocineroDao.agregar(cocinero2)); // INSERT en tablas personal + cocinero

        // Cajero — subclase de Personal con turno "Mañana"
        Cajero cajero1 = new Cajero(0, "Clara", "Ruiz", 32100003L,
                LocalDate.of(1995, 2, 5), LocalDate.of(2023, 1, 15),
                "Mañana");
        cajero1.setId(cajeroDao.agregar(cajero1)); // INSERT en tablas personal + cajero

        // FoodTruck — subclase de UnidadVenta
        // Le asignamos el staff ANTES de guardar para que Hibernate persista la relacion
        FoodTruck ft = new FoodTruck();
        ft.setNombreComercial("El Rincón Criollo");
        ft.setSuperficie(18.0f);
        ft.setCodigo("NACHO00001");
        ft.setResponsableACargo(cocinero1); // muchos-a-uno: responsable del FoodTruck
        ft.setPatente("NAC001");
        ft.setRequiereConexion(false);
        ft.agregarPersonal(cocinero1); // uno-a-muchos: agrega cocinero1 al staff
        ft.agregarPersonal(cocinero2); // uno-a-muchos: agrega cocinero2 al staff
        ft.agregarPersonal(cajero1);   // uno-a-muchos: agrega cajero1 al staff

        // INSERT en tablas: unidad_venta, food_truck, unidad_venta_staff (tabla intermedia)
        ft.setId(foodTruckDao.agregar(ft));
        assertTrue(ft.getId() > 0, "FoodTruck debe guardarse en la BD");

        // =====================================================================
        // PASO 2: CONSULTA A LA BD
        // =====================================================================

        // SELECT a la BD: trae el FoodTruck con su staff cargado (lazy=false en el mapeo)
        FoodTruck ftRecuperado = foodTruckDao.traer(ft.getId());
        assertNotNull(ftRecuperado, "Debe recuperarse el FoodTruck de la BD");

        // El staff es un Set<Personal> — puede contener Cajeros y Cocineros mezclados
        Set<Personal> staff = ftRecuperado.getStaff();
        assertFalse(staff.isEmpty(), "El staff no debe estar vacio");

        // =====================================================================
        // PASO 3: FILTRAR POR ROL USANDO HERENCIA (instanceof)
        // Gracias a la herencia, podemos preguntar el tipo real de cada Personal
        // =====================================================================
        long cantCocineros = staff.stream().filter(p -> p instanceof Cocinero).count();
        long cantCajeros   = staff.stream().filter(p -> p instanceof Cajero).count();

        // =====================================================================
        // PASO 4: MOSTRAR RESULTADO
        // =====================================================================
        System.out.println("Unidad: " + ftRecuperado.getNombreComercial());
        System.out.println("Staff total: " + staff.size() + " personas");
        System.out.println("  Cocineros: " + cantCocineros);
        System.out.println("  Cajeros  : " + cantCajeros);

        for (Personal p : staff) {
            // Polimorfismo: segun el tipo real, mostramos atributos especificos de cada subclase
            String rol = (p instanceof Cocinero)
                    ? "Cocinero [esp: " + ((Cocinero) p).getEspecialidad() + "]"
                    : (p instanceof Cajero)
                    ? "Cajero [turno: " + ((Cajero) p).getTurno() + "]"
                    : "Personal";
            System.out.println("  - " + p.getNombre() + " " + p.getApellido()
                    + " | DNI: " + p.getDni() + " | " + rol);
        }

        // Verificaciones finales
        assertEquals(2, cantCocineros, "Debe haber 2 cocineros en el staff");
        assertEquals(1, cantCajeros,   "Debe haber 1 cajero en el staff");

        System.out.println("\n========== FIN CU NACHO ==========\n");
    }

    // No cerramos el SessionFactory aqui porque es un singleton compartido entre todos los tests
}
