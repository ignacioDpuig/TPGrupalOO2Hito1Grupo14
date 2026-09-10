package com.grupo14.test;

import com.grupo14.dao.FestivalDao;
import com.grupo14.datos.DatosIniciales;
import com.grupo14.datos.Festival;
import com.grupo14.datos.FoodTruck;
import com.grupo14.datos.PuestoDesarmable;
import com.grupo14.datos.UnidadVenta;

/**
 * TEST DE PERSISTENCIA DEL CANON - Responsable: Leandro
 *
 * CU: Recuperar un festival desde la BD con sus unidades y verificar que el
 *     canon que devuelve el metodo calcularCannon() de cada unidad coincide
 *     con el valor esperado calculado a mano.
 *
 * Los datos son sembrados por DatosIniciales.cargar() (seed compartido por
 * el grupo), de modo que este test NO crea datos: solo consulta y valida.
 *
 * Formula del canon segun el tipo de unidad (polimorfismo):
 *  - FoodTruck        : superficie * costoSuperficie (+ plusElectricidad si requiere conexion).
 *  - PuestoDesarmable : superficie * costoSuperficie + tiempoMontaje * costoMontaje.
 */
public class LeandroTest {

    public static void main(String[] args) throws Exception {
        System.out.println("=== INICIO TEST PERSISTENCIA CANON FESTIVAL ===");

        // --- 1. SEED: se cargan los datos compartidos del grupo ---
        DatosIniciales.Resultado datos = DatosIniciales.cargar();
        checkTrue(datos.festival.getId() > 0, "Festival persistido");
        checkTrue(datos.foodTruck.getId() > 0, "FoodTruck persistido");
        checkTrue(datos.puesto.getId() > 0, "Puesto persistido");

        // --- 2. SELECT: recuperamos el festival desde la BD con sus unidades ---
        FestivalDao festivalDao = new FestivalDao();
        Festival festivalDb = festivalDao.traer(datos.festival.getId());
        checkNotNull(festivalDb, "Festival recuperado");
        checkEquals(2, festivalDb.getUnidades().size(), "Festival recupera sus 2 unidades");

        // --- 3. VERIFICACION del canon por unidad ---
        // Para cada unidad recuperada calculamos el canon esperado a mano y lo
        // comparamos contra el resultado de calcularCannon() (que resuelve la
        // formula segun la subclase real: polimorfismo).
        double canonTotal = 0.0;
        double canonEsperadoTotal = 0.0;

        for (UnidadVenta unidad : festivalDb.getUnidades()) {
            double canon = unidad.calcularCannon();
            canonTotal += canon;

            if (unidad instanceof FoodTruck foodTruckDb) {
                // FoodTruck: superficie * costoSuperficie + plus electricidad (si corresponde).
                double esperado = foodTruckDb.getSuperficie() * festivalDb.getCostoSuperficie();
                double plus = foodTruckDb.getRequiereConexion() ? festivalDb.getPlusElectricidad() : 0.0;
                esperado += plus;
                canonEsperadoTotal += esperado;

                System.out.println(
                        "[FoodTruck] " + foodTruckDb.getNombreComercial() +
                                " => canon = (" + foodTruckDb.getSuperficie() +
                                " * " + festivalDb.getCostoSuperficie() + ")" +
                                " + " + plus +
                                " = " + esperado +
                                " | calculado por metodo = " + canon
                );

                checkEquals(esperado, canon, 0.001, "Canon FoodTruck");
            }

            if (unidad instanceof PuestoDesarmable puestoDb) {
                // PuestoDesarmable: superficie * costoSuperficie + tiempoMontaje * costoMontaje.
                double esperado = puestoDb.getSuperficie() * festivalDb.getCostoSuperficie()
                        + puestoDb.getTiempoMontajeMinutos() * festivalDb.getCostoMontaje();
                canonEsperadoTotal += esperado;

                System.out.println(
                        "[PuestoDesarmable] " + puestoDb.getNombreComercial() +
                                " => canon = (" + puestoDb.getSuperficie() +
                                " * " + festivalDb.getCostoSuperficie() + ")" +
                                " + (" + puestoDb.getTiempoMontajeMinutos() +
                                " * " + festivalDb.getCostoMontaje() + ")" +
                                " = " + esperado +
                                " | calculado por metodo = " + canon
                );

                checkEquals(esperado, canon, 0.001, "Canon PuestoDesarmable");
            }
        }

        // --- 4. VERIFICACION del canon total del festival ---
        System.out.println(
                "Canon total festival = " + canonEsperadoTotal +
                        " | calculado sumando unidades = " + canonTotal
        );

        checkEquals(canonEsperadoTotal, canonTotal, 0.001, "Canon total del festival");
        System.out.println("=== TEST OK ===");
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

    /** Falla si los dos valores decimales difieren mas que la tolerancia dada. */
    private static void checkEquals(double esperado, double actual, double tolerancia, String mensaje) {
        if (Math.abs(esperado - actual) > tolerancia) {
            throw new RuntimeException("FALLO: " + mensaje + " | esperado=" + esperado + " actual=" + actual);
        }
        System.out.println("OK: " + mensaje);
    }
}
