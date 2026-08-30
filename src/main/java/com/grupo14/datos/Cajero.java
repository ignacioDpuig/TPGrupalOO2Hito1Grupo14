package com.grupo14.datos;

import java.time.LocalDate;

public class Cajero extends Personal {
    private String turno;

    public Cajero() {
    }

    public Cajero(int id, String nombre, String apellido, long dni,
                  LocalDate fechaNacimiento, LocalDate fechaIngreso,
                  String turno) {
        super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public double calcularHaberes(ConfiguracionCostos configuracionFestival) {
        return configuracionFestival.getSueldoBase()
                + calcularAntiguedad() * configuracionFestival.getPlusAntiguedad();
    }

    @Override
    public String toString() {
        return "Cajero [turno=" + turno + "]";
    }
}
