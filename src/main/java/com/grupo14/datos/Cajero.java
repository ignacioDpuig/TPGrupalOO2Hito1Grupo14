package com.grupo14.datos;

import java.time.LocalDate;

public class Cajero extends Personal {
    private String turno;

    public Cajero() {
    }

    public Cajero(int id, String nombre, String apellido, long dni,
                  LocalDate fechaNacimiento, LocalDate fechaIngreso,
                  String turno) throws Exception {
        super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
        setTurno(turno);
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public double calcularHaberes(double sueldoBase) {
        return sueldoBase + calcularAntiguedad() * 150.0;
    }

    @Override
    public String toString() {
        return "Cajero [turno=" + turno + "]";
    }
}
