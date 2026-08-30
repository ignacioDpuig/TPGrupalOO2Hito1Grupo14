package com.grupo14.datos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Personal {
    protected int id;
    protected String nombre;
    protected String apellido;
    protected long dni;
    protected LocalDate fechaNacimiento;
    protected LocalDate fechaIngreso;

    public Personal() {
    }

    public Personal(int id, String nombre, String apellido, long dni,
                    LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int calcularAntiguedad() {
        LocalDate fechaActual = LocalDate.now();
        int antiguedad = (int) ChronoUnit.YEARS.between(fechaIngreso, fechaActual);
        return antiguedad;
    }

    public abstract double calcularHaberes(ConfiguracionCostos configuracionFestival);

    @Override
    public String toString() {
        return "Personal [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido +
               ", dni=" + dni + ", fechaNacimiento=" + fechaNacimiento +
               ", fechaIngreso=" + fechaIngreso + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        Personal other = (Personal) obj;
        return dni == other.dni;
    }
}
