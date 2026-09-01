package com.grupo14.datos;

import java.time.LocalDate;

public class Cocinero extends Personal {
    private String especialidad;
    private int categoria;

    public Cocinero() {
    }

    public Cocinero(int id, String nombre, String apellido, long dni,
                    LocalDate fechaNacimiento, LocalDate fechaIngreso,
                    String especialidad, int categoria) {
        super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
        this.especialidad = especialidad;
        this.categoria = categoria;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    @Override
    public double calcularHaberes(double sueldoBase) {
        return sueldoBase + 500.0 * getCategoria(); // plus fijo por categoría
    }

    @Override
    public String toString() {
        return "Cocinero [especialidad=" + especialidad + ", categoria=" + categoria + "]";
    }
}
