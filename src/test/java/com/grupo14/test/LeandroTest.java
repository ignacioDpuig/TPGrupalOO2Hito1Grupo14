package com.grupo14.test;

import com.grupo14.dao.CajeroDao;
import com.grupo14.dao.CocineroDao;
import com.grupo14.dao.FestivalDao;
import com.grupo14.dao.FoodTruckDao;
import com.grupo14.dao.PuestoDesarmableDao;
import com.grupo14.datos.Cajero;
import com.grupo14.datos.Cocinero;
import com.grupo14.datos.Festival;
import com.grupo14.datos.FoodTruck;
import com.grupo14.datos.Personal;
import com.grupo14.datos.PuestoDesarmable;
import com.grupo14.datos.UnidadVenta;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class LeandroTest {

    public static void main(String[] args) throws Exception {
        FestivalDao festivalDao = new FestivalDao();
        CocineroDao cocineroDao = new CocineroDao();
        CajeroDao cajeroDao = new CajeroDao();
        FoodTruckDao foodTruckDao = new FoodTruckDao();
        PuestoDesarmableDao puestoDao = new PuestoDesarmableDao();

        System.out.println("=== INICIO TEST PERSISTENCIA CANON FESTIVAL ===");

        Festival festival = new Festival();
        festival.setNombre("Festival Canon");
        festival.setTemporada("Verano");
        festival.setFechaInicio(LocalDate.of(2026, 1, 10));
        festival.setFechaFin(LocalDate.of(2026, 1, 20));
        festival.setCostoSuperficie(100.0);
        festival.setCostoMontaje(10.0);
        festival.setPlusElectricidad(500.0);
        festival.setSueldoBase(1000.0);
        festival.setUnidades(new HashSet<>());
        festival.setId(festivalDao.agregar(festival));
        checkTrue(festival.getId() > 0, "Festival persistido");

        Cocinero cocineroFt = new Cocinero(
                0, "Mario", "Rossi", 30111222L,
                LocalDate.of(1988, 5, 10), LocalDate.now().minusYears(6),
                "Hamburguesas", 2
        );
        cocineroFt.setId(cocineroDao.agregar(cocineroFt));

        Cajero cajeroFt = new Cajero(
                0, "Ana", "Lopez", 32111333L,
                LocalDate.of(1990, 3, 22), LocalDate.now().minusYears(4),
                "MANANA"
        );
        cajeroFt.setId(cajeroDao.agregar(cajeroFt));

        Set<Personal> staffFt = new HashSet<>();
        staffFt.add(cocineroFt);
        staffFt.add(cajeroFt);

        FoodTruck foodTruck = new FoodTruck(
                0, "BurgerBus", cocineroFt, 20.0f,
                staffFt, "ABC123", true,
                new HashSet<>(), new HashSet<>(), festival
        );
        foodTruck.setId(foodTruckDao.agregar(foodTruck));
        checkTrue(foodTruck.getId() > 0, "FoodTruck persistido");

        Cocinero cocineroPd = new Cocinero(
                0, "Lucia", "Perez", 33111444L,
                LocalDate.of(1987, 8, 15), LocalDate.now().minusYears(3),
                "Pizzas", 1
        );
        cocineroPd.setId(cocineroDao.agregar(cocineroPd));

        Cajero cajeroPd = new Cajero(
                0, "Juan", "Gomez", 34111555L,
                LocalDate.of(1992, 12, 1), LocalDate.now().minusYears(2),
                "NOCHE"
        );
        cajeroPd.setId(cajeroDao.agregar(cajeroPd));

        Set<Personal> staffPd = new HashSet<>();
        staffPd.add(cocineroPd);
        staffPd.add(cajeroPd);

        PuestoDesarmable puesto = new PuestoDesarmable(
                0, "PizzaSur", cajeroPd, 15.0f,
                staffPd, 2, 60.0f,
                new HashSet<>(), new HashSet<>(), festival
        );
        puesto.setId(puestoDao.agregar(puesto));
        checkTrue(puesto.getId() > 0, "Puesto persistido");

        festival.agregarUnidad(foodTruck);
        festival.agregarUnidad(puesto);

        Festival festivalDb = festivalDao.traer(festival.getId());
        checkNotNull(festivalDb, "Festival recuperado");
        checkEquals(2, festivalDb.getUnidades().size(), "Festival recupera sus 2 unidades");

        double canonTotal = 0.0;
        double canonEsperadoTotal = 0.0;

        for (UnidadVenta unidad : festivalDb.getUnidades()) {
            double canon = unidad.calcularCannon();
            canonTotal += canon;

            if (unidad instanceof FoodTruck foodTruckDb) {
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

        System.out.println(
                "Canon total festival = " + canonEsperadoTotal +
                        " | calculado sumando unidades = " + canonTotal
        );

        checkEquals(canonEsperadoTotal, canonTotal, 0.001, "Canon total del festival");
        System.out.println("=== TEST OK ===");
    }

    private static void checkTrue(boolean condition, String mensaje) {
        if (!condition) {
            throw new RuntimeException("FALLO: " + mensaje);
        }
        System.out.println("OK: " + mensaje);
    }

    private static void checkNotNull(Object value, String mensaje) {
        if (value == null) {
            throw new RuntimeException("FALLO: " + mensaje);
        }
        System.out.println("OK: " + mensaje);
    }

    private static void checkEquals(int esperado, int actual, String mensaje) {
        if (esperado != actual) {
            throw new RuntimeException("FALLO: " + mensaje + " | esperado=" + esperado + " actual=" + actual);
        }
        System.out.println("OK: " + mensaje);
    }

    private static void checkEquals(double esperado, double actual, double tolerancia, String mensaje) {
        if (Math.abs(esperado - actual) > tolerancia) {
            throw new RuntimeException("FALLO: " + mensaje + " | esperado=" + esperado + " actual=" + actual);
        }
        System.out.println("OK: " + mensaje);
    }
}