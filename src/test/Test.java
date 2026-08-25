package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Cajero;
import modelo.Cocinero;
import modelo.ConfiguracionCostos;
import modelo.DetallePedido;
import modelo.Encargado;
import modelo.Festival;
import modelo.FoodTruck;
import modelo.Funciones;
import modelo.Pedido;
import modelo.Personal;
import modelo.Plato;
import modelo.PuestoDesarmable;
/*import modelo.ReporteMayoresCanon;
import modelo.ReporteVenta;
import modelo.Sistema;*/
import modelo.UnidadVenta;

public class Test {

    public static void main(String[] args) {
        try {
            Sistema sistema = new Sistema();
            ConfiguracionCostos costos = new ConfiguracionCostos(1, 500, 10, 2000, 5000, 100000, 500);
            
            //Alta de festival, personal y unidadVenta en la lista de Sistema
            sistema.altaFestival("Festival del Sabor", "Verano", LocalDate.of(2027, 1, 15), LocalDate.of(2027, 1, 30), new ArrayList<>());
            Festival festivalActual = sistema.buscarFestivalPorId(1);

            System.out.println("\n--- CU 1---");
            
            sistema.altaPersonal( "Juan", "Perez", 12345678, LocalDate.of(1980, 5, 20),LocalDate.of(1980, 5, 20), 5);
            sistema.altaPersonal( "Juan1", "Perez1", 87654321, LocalDate.of(1980, 5, 20),LocalDate.of(1980, 5, 20), 5);

            sistema.altaPersonal("Cocinero", "uno", 11111111, LocalDate.of(1990, 8, 15),LocalDate.of(1980, 5, 20), "Pastas", 2);
            sistema.altaPersonal("Cocinero", "dos", 22222222, LocalDate.of(1990, 8, 15),LocalDate.of(1980, 5, 20), "Ravioli", 1);

            sistema.altaPersonal("Cajero", "uno", 11223344, LocalDate.of(1995, 1, 10),LocalDate.of(2025, 6, 20), "Noche");
            sistema.altaPersonal("Cajero", "dos", 44332211, LocalDate.of(1995, 1, 10),LocalDate.of(2025, 5, 20), "Tarde");

            sistema.altaUnidadVenta("Puesto", sistema.buscarPersonalPorDni(12345678), 1f, "11Puesto2026",new ArrayList<>(), new ArrayList<>(), 10f, 2, 1f, costos, new ArrayList<>());
            sistema.altaUnidadVenta("Foodtruck", sistema.buscarPersonalPorDni(87654321), 1f, "11Foodtruck2026", new ArrayList<>(), new ArrayList<>(), 10f, "AAA111", true, costos, new ArrayList<>()); 
            
            sistema.altaUnidadVenta("Puesto1", sistema.buscarPersonalPorDni(12345678), 1f, "11Puesto12026",new ArrayList<>(), new ArrayList<>(), 10f, 2, 1f, costos, new ArrayList<>());
            sistema.altaUnidadVenta("Foodtruck1", sistema.buscarPersonalPorDni(87654321), 1f, "11Foodtruck12026", new ArrayList<>(), new ArrayList<>(), 10f, "AAA111", true, costos, new ArrayList<>()); 
            
            //Se agregan las unidades al festival creado
            festivalActual.agregarUnidad(sistema.buscarUnidadVentaPorCodigo("11Puesto2026"));
            festivalActual.agregarUnidad(sistema.buscarUnidadVentaPorCodigo("11Foodtruck2026"));
            festivalActual.agregarUnidad(sistema.buscarUnidadVentaPorCodigo("11Puesto12026"));
            festivalActual.agregarUnidad(sistema.buscarUnidadVentaPorCodigo("11Foodtruck12026"));
            //Se le agrega el personal a cada unidad de ese festival
            
            //FoodTruck
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").agregarPersonal(sistema.buscarPersonalPorDni(11111111));
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").agregarPersonal(sistema.buscarPersonalPorDni(11223344));
            
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck12026").agregarPersonal(sistema.buscarPersonalPorDni(11111111));
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck12026").agregarPersonal(sistema.buscarPersonalPorDni(11223344));
            //PuestoDesarmable
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").agregarPersonal(sistema.buscarPersonalPorDni(22222222));
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").agregarPersonal(sistema.buscarPersonalPorDni(44332211));
            
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto12026").agregarPersonal(sistema.buscarPersonalPorDni(22222222));
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto12026").agregarPersonal(sistema.buscarPersonalPorDni(44332211));
            
            //Se agregan platos a cada Unidad del festival
            Plato plato1 = new Plato( "Comida 10 pesos", 10, 5);
            Plato plato2 = new Plato( "Comida 20 pesos", 20, 10);
            Plato plato3 = new Plato( "Comida 30 pesos", 30, 15);
            Plato plato4 = new Plato( "Comida 40 pesos", 40, 20);
            
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").agregarPlato(plato1);
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").agregarPlato(plato2);

            festivalActual.buscarUnidadVentaPorCodigo("11Puesto12026").agregarPlato(plato1);
            festivalActual.buscarUnidadVentaPorCodigo("11Puesto12026").agregarPlato(plato2);
            
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").agregarPlato(plato3);
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").agregarPlato(plato4);
            
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck12026").agregarPlato(plato3);
            festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck12026").agregarPlato(plato4);
            
        	System.out.println(festivalActual);

            System.out.println("\n--- CU 2---");
            System.out.println("Buscando personal con DNI 12345678:");
            System.out.println("| ID   | NOMBRE               | DNI        | INGRESO    | NACIMIENTO |ACARGO| TUNO        | CAT  | ESPEC.     |");
            System.out.println(sistema.buscarPersonalPorDni(12345678));
            System.out.println("\\nBuscando unidad con código 11Foodtruck2026 :\n " + sistema.buscarUnidadVentaPorCodigo("11Foodtruck2026"));

            // CU 3: Cálculo de Canon
            System.out.println("\n--- CU 3: Cálculo de Canon ---");
            System.out.printf("Canon para Puesto Nombre: $%.2f\n",  sistema.buscarUnidadVentaPorCodigo("11Puesto2026").calcularCannon(costos));
            System.out.printf("Canon para Food Truck Nombre1: $%.2f\n",sistema.buscarUnidadVentaPorCodigo("11Foodtruck2026").calcularCannon(costos));
          
            // CU 4: Liquidación de Haberes
            System.out.println("\n--- CU 4: Liquidación de Haberes ---");
            System.out.printf("Sueldo para Cocinero: $%.2f\n", sistema.buscarPersonalPorDni(22222222).calcularHaberes(costos));
            System.out.printf("Sueldo para Cajero: $%.2f\n", sistema.buscarPersonalPorDni(44332211).calcularHaberes(costos));
                        


            // CU 5: Registro de Pedido Validado
            System.out.println("\n--- CU 5: Registro de Pedido Validado ---");
            
            //PUESTO
            List<DetallePedido> detalle1 = new ArrayList<>();
            detalle1.add(new DetallePedido(plato1, 1)); 
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Puesto2026"), festivalActual, detalle1);
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Puesto12026"), festivalActual, detalle1);

            System.out.println(festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").getPedidos().getFirst());
            List<DetallePedido> detalle2 = new ArrayList<>();
            detalle2.add(new DetallePedido(plato2, 2)); 
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Puesto2026"), festivalActual, detalle2);
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Puesto12026"), festivalActual, detalle2);

            //Foodtruck
            List<DetallePedido> detalle3 = new ArrayList<>();
            detalle3.add(new DetallePedido(plato3, 3)); 
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Foodtruck2026"), festivalActual, detalle3);
            sistema.agregarPedido(LocalDate.of(2026, 6, 6),sistema.buscarUnidadVentaPorCodigo("11Foodtruck12026"), festivalActual, detalle3);

            List<DetallePedido> detalle4 = new ArrayList<>();
            detalle4.add(new DetallePedido(plato4, 4));
            sistema.agregarPedido(LocalDate.of(2026, 5, 6),sistema.buscarUnidadVentaPorCodigo("11Foodtruck2026"), festivalActual, detalle4);
            sistema.agregarPedido(LocalDate.of(2026, 5, 6),sistema.buscarUnidadVentaPorCodigo("11Foodtruck12026"), festivalActual, detalle4);       

                   
            // CU 6: Reporte de Recaudación
            System.out.println("\n--- CU 6: Reporte de Recaudación ---");
            List<ReporteVenta> reporteRecaudacion = sistema.reporteRecaudacion(festivalActual);
            System.out.println("Recaudación para el festival '" + festivalActual.getNombre() + "':");
            for (ReporteVenta rv : reporteRecaudacion) {
                System.out.printf(" - Unidad: %s, Recaudación: $%.2f\n", rv.getUnidad().getNombreComercial(), rv.getRecaudacionTotal());
            }
           
            // CU 7: Filtro de Personal por Edad
            System.out.println("\n--- CU 7: Filtro de Personal por Edad ---");
            LocalDate fechaDesde = LocalDate.of(1980, 1, 1);
            LocalDate fechaHasta = LocalDate.of(1991, 1, 1);
            List<Personal> personalFiltrado = sistema.filtrarPorFechaDeNacimiento(fechaDesde, fechaHasta);
            System.out.println("Personal nacido entre " + fechaDesde + " y " + fechaHasta + ":");
            System.out.println("| ID   | NOMBRE               | DNI        | INGRESO    | NACIMIENTO |ACARGO| TUNO        | CAT  | ESPEC.     |");
            for (Personal p : personalFiltrado) {
                System.out.println(p);
            }
          
            // CU 8 y 9: Cálculo de Rentabilidad Neta
            System.out.println("\n--- CU 8 y 9: Cálculo de Rentabilidad Neta ---");
            double rentabilidadNeta = festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").calcularRentabilidadNeta();
            System.out.printf("Rentabilidad Neta para '%s': $%.2f\n", festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").getNombreComercial(), rentabilidadNeta);
            
            double rentabilidadFechas =  festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").calcularRentabilidadNetaEntreFechas(LocalDate.of(2026, 6, 6),LocalDate.of(2026, 6, 6));
            System.out.printf("Rentabilidad Neta (entre fechas) para '%s': $%.2f\n", festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").getNombreComercial(), rentabilidadFechas);
         
            // CU 10: Ranking de Unidades (simulado con el reporte de recaudación)
            System.out.println("\n--- CU 10: Ranking de Unidades por Recaudación ---");
            System.out.println("Ranking de unidades:");
            List<UnidadVenta> rankingUnidades = sistema.rankingDeUnidades();
            for(UnidadVenta unidad : rankingUnidades) {
            	System.out.println(unidad + "Recaudacion $" + unidad.calcularRecaudacionTotal());
            }
          
            //CU 11: Plato Estrella
            System.out.println("\n--- CU 11: Plato Estrella ---");
            System.out.printf("El plato estrella de '%s' es: %s\n", festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").getNombreComercial(), festivalActual.buscarUnidadVentaPorCodigo("11Foodtruck2026").platoEstrella());
            System.out.printf("El plato estrella de '%s' es: %s\n", festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").getNombreComercial(), festivalActual.buscarUnidadVentaPorCodigo("11Puesto2026").platoEstrella());

         
            // CU 12: Auditoría de Personal del Festival
            System.out.println("\n--- CU 12: Auditoría de Personal del Festival ---");
            List<Personal> personalFestival = sistema.auditoriaPersonalFestival(festivalActual);
            System.out.println("Personal que trabajó en el festival '" + festivalActual.getNombre() + "':");
            System.out.println("| ID   | NOMBRE               | DNI        | INGRESO    | NACIMIENTO |ACARGO| TUNO        | CAT  | ESPEC.     |");

            for (Personal p : personalFestival) {
                System.out.println(p);
            }
            	
            System.out.println("\n--- CU 13: Unidades con Mayor Canon ---");
            List<ReporteMayoresCanon> reporteCanon = festivalActual.calcularMayoresCanon();
            System.out.println("Top 3 unidades con mayor canon:");
            for (ReporteMayoresCanon rmc : reporteCanon) {
                System.out.printf(" - %s (%s), Tipo: %s, Canon: $%.2f\n", rmc.getNombreComercial(), rmc.getCodigo(), rmc.getTipoUnidad(), rmc.getCanon());
            }
            List<Personal> staf =festivalActual.getUnidades().getFirst().getStaff();
            		for(Personal personal : staf) {
                        System.out.println(personal);

            		}
        } catch (Exception e) {
            System.err.println("\nOcurrió un error durante la ejecución del test: " + e.getMessage() + " ###");
        }
    }
}