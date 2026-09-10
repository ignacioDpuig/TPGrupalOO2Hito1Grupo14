package com.grupo14.test;

import com.grupo14.dao.FoodTruckDao;
import com.grupo14.dao.PedidoDao;
import com.grupo14.datos.DatosIniciales;
import com.grupo14.datos.DetallePedido;
import com.grupo14.datos.FoodTruck;
import com.grupo14.datos.Pedido;
import com.grupo14.datos.PuestoDesarmable;

import java.util.Set;

/**
 * Caso de Uso - Responsable: Joaquin
 *
 * CU: Dado un festival, listar todos sus pedidos con los detalles de cada uno,
 *     distinguiendo si la unidad de venta es un FoodTruck o un PuestoDesarmable.
 *
 * Los datos son sembrados por DatosIniciales.cargar() (seed compartido por
 * el grupo). El seed carga DOS festivales, lo que permite demostrar que las
 * consultas filtran correctamente por festival y no mezclan datos entre uno
 * y otro.
 *
 * Consultas de los DAOs que ejercita:
 *  - PedidoDao.traerPorFestival(idFestival) : pedidos que pertenecen a un festival.
 *  - PedidoDao.traerPorUnidad(idUnidad)     : pedidos que pertenecen a una unidad.
 *  - FoodTruckDao.traerConConexion()        : food trucks que requieren conexion electrica.
 */
public class JoaquinTest {

    public static void main(String[] args) throws Exception {
        System.out.println("\n========== CU JOAQUIN: Pedidos de un festival con detalles ==========\n");

        // =====================================================================
        // PASO 1 - SEED: se cargan los datos compartidos del grupo.
        // El seed devuelve las entidades ya creadas (con su id) para poder
        // consultarlas: el festival principal y el festival secundario, mas
        // sus unidades de venta.
        // =====================================================================
        DatosIniciales.Resultado datos = DatosIniciales.cargar();

        PedidoDao pedidoDao = new PedidoDao();
        FoodTruckDao foodTruckDao = new FoodTruckDao();

        int idFestival     = datos.festival.getId();
        int idFestival2    = datos.festivalSecundario.getId();
        int idFoodTruck    = datos.foodTruck.getId();
        int idPuesto       = datos.puesto.getId();

        // =====================================================================
        // PASO 2 - CONSULTA por festival: los pedidos deben filtrarse bien.
        // El festival principal tiene 3 pedidos; el secundario tiene 1.
        // Que los totales sean distintos demuestra que la consulta no mezcla
        // los pedidos de un festival con los del otro.
        // =====================================================================
        Set<Pedido> pedidos = pedidoDao.traerPorFestival(idFestival);
        checkTrue(!pedidos.isEmpty(), "Debe haber pedidos para el festival principal");
        checkEquals(3, pedidos.size(), "El festival principal debe tener 3 pedidos");

        Set<Pedido> pedidosFestival2 = pedidoDao.traerPorFestival(idFestival2);
        checkEquals(1, pedidosFestival2.size(), "El segundo festival debe tener 1 solo pedido");

        System.out.println("Festival: " + datos.festival.getNombre());
        System.out.println("Total pedidos: " + pedidos.size());

        // =====================================================================
        // PASO 3 - Recorrido de pedidos y detalles.
        // Para cada pedido mostramos el tipo real de su unidad (herencia:
        // FoodTruck o PuestoDesarmable) y sumamos la recaudacion a partir de
        // los detalles (precio de venta * cantidad).
        // =====================================================================
        double recaudacionTotal = 0;
        for (Pedido p : pedidos) {
            String tipoUnidad = (p.getUnidad() instanceof FoodTruck)
                    ? "FoodTruck"
                    : (p.getUnidad() instanceof PuestoDesarmable)
                    ? "PuestoDesarmable"
                    : "UnidadVenta";

            System.out.println("\n  Pedido #" + p.getId()
                    + " | Fecha: " + p.getFecha()
                    + " | Unidad: " + p.getUnidad().getNombreComercial()
                    + " [" + tipoUnidad + "]");

            for (DetallePedido d : p.getDetalles()) {
                double subtotal = d.getPlato().getPrecioVenta() * d.getCantidad();
                recaudacionTotal += subtotal;
                System.out.println("    - " + d.getPlato().getNombre()
                        + " x" + d.getCantidad()
                        + " = $" + String.format("%.2f", subtotal));
            }
        }

        System.out.println("\n  Recaudacion total del festival: $" + String.format("%.2f", recaudacionTotal));
        checkTrue(recaudacionTotal > 0, "La recaudacion debe ser mayor a 0");

        // =====================================================================
        // PASO 4 - CONSULTA por unidad: los pedidos deben repartirse segun la
        // unidad a la que pertenecen. En el festival principal el FoodTruck
        // tiene 2 pedidos y el Puesto tiene 1.
        // =====================================================================
        Set<Pedido> pedidosDelFoodTruck = pedidoDao.traerPorUnidad(idFoodTruck);
        checkEquals(2, pedidosDelFoodTruck.size(),
                "El FoodTruck del festival principal debe tener 2 pedidos");

        Set<Pedido> pedidosDelPuesto = pedidoDao.traerPorUnidad(idPuesto);
        checkEquals(1, pedidosDelPuesto.size(),
                "El Puesto del festival principal debe tener 1 pedido");

        System.out.println("\n  Pedidos por unidad:");
        System.out.println("    - " + datos.foodTruck.getNombreComercial() + ": " + pedidosDelFoodTruck.size() + " pedidos");
        System.out.println("    - " + datos.puesto.getNombreComercial() + ": " + pedidosDelPuesto.size() + " pedidos");

        // =====================================================================
        // PASO 5 - CONSULTA por atributo: food trucks que requieren conexion.
        // El seed crea dos food trucks con conexion (uno por festival), por lo
        // que la consulta debe devolver al menos esos dos, y todos deben tener
        // requiereConexion en true.
        // =====================================================================
        Set<FoodTruck> conConexion = foodTruckDao.traerConConexion();
        checkTrue(conConexion.size() >= 2,
                "Deben aparecer al menos los 2 food trucks con conexion del seed");
        checkTrue(conConexion.stream().allMatch(FoodTruck::getRequiereConexion),
                "Todos los food trucks devueltos deben requerir conexion");

        System.out.println("\n  Food trucks con conexion electrica: " + conConexion.size());
        for (FoodTruck f : conConexion) {
            System.out.println("    - " + f.getNombreComercial() + " (patente " + f.getPatente() + ")");
        }

        System.out.println("\n========== FIN CU JOAQUIN - TEST OK ==========\n");
    }

    // =========================================================================
    // Metodos de comprobacion. Si la condicion esperada no se cumple lanzan
    // una excepcion (el test corta y falla); si se cumple imprimen "OK" con
    // el mensaje correspondiente.
    // =========================================================================

    /** Falla si la condicion es false. */
    private static void checkTrue(boolean condition, String mensaje) {
        if (!condition) {
            throw new RuntimeException("FALLO: " + mensaje);
        }
        System.out.println("OK: " + mensaje);
    }

    /** Falla si los dos enteros no son iguales. */
    private static void checkEquals(int esperado, int actual, String mensaje) {
        if (esperado != actual) {
            throw new RuntimeException("FALLO: " + mensaje + " | esperado=" + esperado + " actual=" + actual);
        }
        System.out.println("OK: " + mensaje);
    }
}
