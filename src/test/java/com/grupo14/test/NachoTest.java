package com.grupo14.test;

import com.grupo14.dao.FestivalDao;
import com.grupo14.dao.PedidoDao;
import com.grupo14.datos.Cajero;
import com.grupo14.datos.Cocinero;
import com.grupo14.datos.DatosIniciales;
import com.grupo14.datos.DetallePedido;
import com.grupo14.datos.Festival;
import com.grupo14.datos.FoodTruck;
import com.grupo14.datos.Pedido;
import com.grupo14.datos.Personal;
import com.grupo14.datos.PuestoDesarmable;
import com.grupo14.datos.UnidadVenta;

import java.util.Set;

/**
 * TEST DE INTEGRACION - Responsable: Ignacio Puig
 *
 * CU: Recuperar desde la BD un Festival completo con sus unidades de venta
 *     asignadas (FoodTruck y PuestoDesarmable), cada una con su staff, sus
 *     platos y sus pedidos con detalles, y verificar que las relaciones y
 *     los calculos de negocio quedaron correctamente persistidos.
 *
 * Los datos son sembrados por DatosIniciales.cargar() (seed compartido por
 * el grupo), de modo que este test NO crea datos: solo consulta y valida.
 *
 * Conceptos de OO / persistencia que demuestra:
 *  - HERENCIA: Personal -> Cajero / Cocinero.  UnidadVenta -> FoodTruck / PuestoDesarmable.
 *  - RELACION Festival 1..* UnidadVenta (FK id_festival en UnidadVenta).
 *  - RELACION UnidadVenta *..* Personal (staff) y *..* Plato (menu).
 *  - RELACION Festival/Unidad 1..* Pedido 1..* DetallePedido.
 *  - POLIMORFISMO: calcularCannon() distinto en cada subclase de UnidadVenta.
 */
public class NachoTest {

    public static void main(String[] args) throws Exception {
        System.out.println("\n========== CU NACHO (INTEGRACION): Festival con unidades asignadas ==========\n");

        // =====================================================================
        // PASO 1 - SEED: se cargan los datos compartidos del grupo.
        // DatosIniciales.cargar() inserta en la BD el festival, el personal,
        // los platos, las unidades y los pedidos, y devuelve las entidades ya
        // creadas (con su id) para poder consultarlas despues.
        // =====================================================================
        DatosIniciales.Resultado datos = DatosIniciales.cargar();

        // Comprobamos que las entidades principales quedaron persistidas
        // (un id > 0 significa que la BD les asigno clave primaria).
        checkTrue(datos.festival.getId() > 0, "El festival debe guardarse en la BD");
        checkTrue(datos.foodTruck.getId() > 0, "El FoodTruck debe guardarse en la BD");
        checkTrue(datos.puesto.getId() > 0, "El Puesto debe guardarse en la BD");

        // DAOs que vamos a usar para leer de la base (capa de acceso a datos).
        FestivalDao festivalDao = new FestivalDao();
        PedidoDao pedidoDao = new PedidoDao();

        // =====================================================================
        // PASO 2 - SELECT: recuperamos el festival desde la BD por su id.
        // Al traerlo, Hibernate carga tambien el set de unidades asociadas
        // (relacion Festival 1..* UnidadVenta).
        // =====================================================================
        Festival festivalBd = festivalDao.traer(datos.festival.getId());
        checkNotNull(festivalBd, "El festival debe recuperarse de la BD");

        Set<UnidadVenta> unidades = festivalBd.getUnidades();
        checkNotNull(unidades, "El festival debe tener su set de unidades");
        checkEquals(2, unidades.size(), "El festival debe tener 2 unidades asignadas");

        // Contamos por tipo para verificar que la HERENCIA se mapeo bien:
        // instanceof distingue la subclase real de cada unidad recuperada.
        long cantFoodTrucks = unidades.stream().filter(u -> u instanceof FoodTruck).count();
        long cantPuestos    = unidades.stream().filter(u -> u instanceof PuestoDesarmable).count();
        checkEquals(1, (int) cantFoodTrucks, "Debe haber 1 FoodTruck asignado");
        checkEquals(1, (int) cantPuestos, "Debe haber 1 PuestoDesarmable asignado");

        // =====================================================================
        // PASO 3 - VERIFICACION de relaciones y calculos por cada unidad.
        // Para cada unidad recuperada comprobamos que conozca su festival,
        // que tenga staff y platos cargados, y que el canon (POLIMORFISMO)
        // se calcule sin errores.
        // =====================================================================
        System.out.println("Festival: " + festivalBd.getNombre() + " (id=" + festivalBd.getId() + ")");
        System.out.println("Unidades asignadas: " + unidades.size());

        double canonTotal = 0;
        for (UnidadVenta u : unidades) {
            // Relacion inversa unidad -> festival (la FK id_festival apunta bien).
            checkNotNull(u.getFestival(), "Cada unidad debe conocer su festival");
            checkEquals(festivalBd.getId(), u.getFestival().getId(),
                    "La unidad debe estar asignada al festival correcto");

            // Las relaciones *..* (staff y platos) deben haberse persistido.
            checkTrue(!u.getStaff().isEmpty(), "La unidad debe tener staff cargado");
            checkTrue(!u.getPlatos().isEmpty(), "La unidad debe tener platos cargados");

            // calcularCannon() es abstracto en UnidadVenta y se resuelve segun
            // la subclase real (FoodTruck suma plus electricidad; el puesto suma
            // el costo por tiempo de montaje). Esto es polimorfismo en accion.
            String tipo = (u instanceof FoodTruck) ? "FoodTruck" : "PuestoDesarmable";
            double canon = u.calcularCannon();
            double sueldos = u.calcularSueldos();
            canonTotal += canon;

            System.out.printf("%n  [%s] %s | superficie=%.1fm2 | staff=%d | platos=%d%n",
                    tipo, u.getNombreComercial(), u.getSuperficie(),
                    u.getStaff().size(), u.getPlatos().size());
            System.out.printf("     canon=$%.2f | sueldos=$%.2f%n", canon, sueldos);
            checkTrue(canon > 0, "El canon de la unidad debe ser mayor a 0");

            // Recorremos el staff distinguiendo el rol real de cada Personal
            // (nuevamente herencia: Cocinero vs Cajero).
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
        // PASO 4 - VERIFICACION de pedidos y recaudacion.
        // Consultamos los pedidos del festival (relacion Festival 1..* Pedido)
        // y recorremos sus detalles (Pedido 1..* DetallePedido) para calcular
        // la recaudacion total (precio de venta * cantidad).
        // =====================================================================
        Set<Pedido> pedidos = pedidoDao.traerPorFestival(festivalBd.getId());
        checkEquals(3, pedidos.size(), "El festival debe tener 3 pedidos");

        double recaudacion = 0;
        int totalDetalles = 0;
        for (Pedido p : pedidos) {
            for (DetallePedido d : p.getDetalles()) {
                recaudacion += d.getPlato().getPrecioVenta() * d.getCantidad();
                totalDetalles++;
            }
        }
        checkEquals(5, totalDetalles, "Deben existir 5 lineas de detalle en total");
        checkTrue(recaudacion > 0, "La recaudacion debe ser mayor a 0");

        System.out.printf("%n  Canon total del festival : $%.2f%n", canonTotal);
        System.out.printf("  Recaudacion total        : $%.2f%n", recaudacion);
        System.out.printf("  Pedidos                  : %d%n", pedidos.size());
        System.out.println("\n========== FIN CU NACHO (INTEGRACION) - TEST OK ==========\n");
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

    /** Falla si el valor es null. */
    private static void checkNotNull(Object value, String mensaje) {
        if (value == null) {
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
