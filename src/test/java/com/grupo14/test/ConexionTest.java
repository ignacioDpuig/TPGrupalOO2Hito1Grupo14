

/*
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.grupo14.datos.Cajero;
import com.grupo14.datos.Cocinero;
import com.grupo14.datos.ConfiguracionCostos;
import com.grupo14.datos.DetallePedido;
import com.grupo14.datos.Encargado;
import com.grupo14.datos.Festival;
import com.grupo14.datos.FoodTruck;
import com.grupo14.datos.Pedido;
import com grupo14.datos.Personal;
import com.grupo14.datos.Plato;
import com.grupo14.datos.PuestoDesarmable;
import com.grupo14.datos.UnidadVenta;
import com.grupo14.util.Funciones;
*/
package com.grupo14.test;

import org.hibernate.Session;
import com.grupo14.dao.HibernateUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConexionTest {

    @Test
    public void probarConexionYCreacionTablas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        assertNotNull(session, "La sesión no debe ser nula");
        System.out.println("¡Conexión exitosa y tablas creadas en MySQL!");
        session.close();
    }
}
    /*
    // CÓDIGO ANTERIOR COMENTADO - Descomentar cuando Sistema esté implementado
    
    public static void mainOLD(String[] args) {
        
        try {
            ConfiguracionCostos costos = new ConfiguracionCostos(1, 500, 10, 2000, 5000, 100000, 500);

            System.out.println("\n--- CU 1---");

            // Alta manual de personal (sin Sistema)
            Encargado encargado1 = new Encargado();
            encargado1.setNombre("Juan");
            encargado1.setApellido("Perez");
            encargado1.setDni(12345678);
            encargado1.setFechaNacimiento(LocalDate.of(1980, 5, 20));
            encargado1.setFechaIngreso(LocalDate.of(1980, 5, 20));

            Encargado encargado2 = new Encargado();
            encargado2.setNombre("Juan1");
            encargado2.setApellido("Perez1");
            encargado2.setDni(87654321);
            encargado2.setFechaNacimiento(LocalDate.of(1980, 5, 20));
            encargado2.setFechaIngreso(LocalDate.of(1980, 5, 20));

            Cocinero cocinero1 = new Cocinero(1, "Cocinero", "uno", 11111111,
                    LocalDate.of(1990, 8, 15), LocalDate.of(1980, 5, 20), "Pastas", 2);
            Cocinero cocinero2 = new Cocinero(2, "Cocinero", "dos", 22222222,
                    LocalDate.of(1990, 8, 15), LocalDate.of(1980, 5, 20), "Ravioli", 1);

            Cajero cajero1 = new Cajero(3, "Cajero", "uno", 11223344,
                    LocalDate.of(1995, 1, 10), LocalDate.of(2025, 6, 20), "Noche");
            Cajero cajero2 = new Cajero(4, "Cajero", "dos", 44332211,
                    LocalDate.of(1995, 1, 10), LocalDate.of(2025, 5, 20), "Tarde");

            // Alta manual de unidades de venta
            PuestoDesarmable puesto1 = new PuestoDesarmable(1, "Puesto", encargado1, 1f,
                    "29Puesto2026", new ArrayList<>(), 2, 1f, costos, new ArrayList<>(), new ArrayList<>());
            FoodTruck foodtruck1 = new FoodTruck(2, "Foodtruck", encargado2, 1f,
                    "29Foodtruck2026", new ArrayList<>(), "AAA111", true, costos, new ArrayList<>(), new ArrayList<>());

            // Alta manual de festival
            Festival festivalActual = new Festival(1, "Festival del Sabor", "Verano",
                    LocalDate.of(2027, 1, 15), LocalDate.of(2027, 1, 30), new ArrayList<>());

            festivalActual.agregarUnidad(puesto1);
            festivalActual.agregarUnidad(foodtruck1);

            // Agregar personal a las unidades
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPersonal(cocinero1);
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPersonal(cajero1);
            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPersonal(cocinero2);
            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPersonal(cajero2);

            // Agregar platos
            Plato plato1 = new Plato("Comida 10 pesos", 10, 5);
            Plato plato2 = new Plato("Comida 20 pesos", 20, 10);
            Plato plato3 = new Plato("Comida 30 pesos", 30, 15);
            Plato plato4 = new Plato("Comida 40 pesos", 40, 20);

            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPlato(plato1);
            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPlato(plato2);
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPlato(plato3);
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPlato(plato4);

            System.out.println(festivalActual);

            // CU 2: Búsqueda
            System.out.println("\n--- CU 2 ---");
            System.out.println("Encargado 1: " + encargado1);
            System.out.println("Unidad Foodtruck: " + festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026"));

            // CU 3: Cálculo de Canon
            System.out.println("\n--- CU 3: Cálculo de Canon ---");
            System.out.printf("Canon para Puesto: $%.2f%n", festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").calcularCannon(costos));
            System.out.printf("Canon para FoodTruck: $%.2f%n", festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").calcularCannon(costos));

            // CU 4: Liquidación de Haberes
            System.out.println("\n--- CU 4: Liquidación de Haberes ---");
            System.out.printf("Sueldo Cocinero: $%.2f%n", cocinero2.calcularHaberes(costos));
            System.out.printf("Sueldo Cajero: $%.2f%n", cajero2.calcularHaberes(costos));

            // CU 5: Registro de Pedido
            System.out.println("\n--- CU 5: Registro de Pedido ---");
            List<DetallePedido> detalle1 = new ArrayList<>();
            detalle1.add(new DetallePedido(plato1, 1));
            Pedido pedido1 = new Pedido(1, LocalDate.of(2027, 1, 20), puesto1, festivalActual, detalle1);
            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPedido(pedido1);
            System.out.println(pedido1);

            List<DetallePedido> detalle2 = new ArrayList<>();
            detalle2.add(new DetallePedido(plato2, 2));
            Pedido pedido2 = new Pedido(2, LocalDate.of(2027, 1, 20), puesto1, festivalActual, detalle2);
            festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").agregarPedido(pedido2);

            List<DetallePedido> detalle3 = new ArrayList<>();
            detalle3.add(new DetallePedido(plato3, 3));
            Pedido pedido3 = new Pedido(3, LocalDate.of(2027, 1, 21), foodtruck1, festivalActual, detalle3);
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPedido(pedido3);

            List<DetallePedido> detalle4 = new ArrayList<>();
            detalle4.add(new DetallePedido(plato4, 4));
            Pedido pedido4 = new Pedido(4, LocalDate.of(2027, 1, 22), foodtruck1, festivalActual, detalle4);
            festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").agregarPedido(pedido4);

            // CU 8 y 9: Rentabilidad Neta
            System.out.println("\n--- CU 8 y 9: Rentabilidad Neta ---");
            double rentabilidadNeta = festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").calcularRentabilidadNeta();
            System.out.printf("Rentabilidad Neta FoodTruck: $%.2f%n", rentabilidadNeta);

            double rentabilidadFechas = festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026")
                    .calcularRentabilidadNetaEntreFechas(LocalDate.of(2027, 1, 21), LocalDate.of(2027, 1, 21));
            System.out.printf("Rentabilidad Neta (entre fechas) FoodTruck: $%.2f%n", rentabilidadFechas);

            // CU 10: Ranking
            System.out.println("\n--- CU 10: Ranking de Unidades ---");
            List<UnidadVenta> unidades = festivalActual.getUnidades();
            unidades.sort((a, b) -> Double.compare(b.calcularRecaudacionTotal(), a.calcularRecaudacionTotal()));
            for (UnidadVenta u : unidades) {
                System.out.printf("%s - Recaudacion: $%.2f%n", u.getNombreComercial(), u.calcularRecaudacionTotal());
            }

            // CU 11: Plato Estrella
            System.out.println("\n--- CU 11: Plato Estrella ---");
            System.out.printf("Plato estrella FoodTruck: %s%n",
                    festivalActual.buscarUnidadVentaPorCodigo("29Foodtruck2026").platoEstrella());
            System.out.printf("Plato estrella Puesto: %s%n",
                    festivalActual.buscarUnidadVentaPorCodigo("29Puesto2026").platoEstrella());

            // CU 12: Auditoría de personal
            System.out.println("\n--- CU 12: Auditoría de Personal ---");
            for (UnidadVenta u : festivalActual.getUnidades()) {
                System.out.println("Staff de " + u.getNombreComercial() + ":");
                for (Personal p : u.getStaff()) {
                    System.out.println("  " + p);
                }
            }

            // CU 6, 7, 13: requieren Sistema - pendientes de implementación
            System.out.println("\n--- CU 6, 7, 13: pendientes (requieren Sistema) ---");

        } catch (Exception e) {
            System.err.println("\nError: " + e.getMessage());
            e.printStackTrace();
        }
    }
    */

