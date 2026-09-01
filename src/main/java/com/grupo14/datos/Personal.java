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
                    LocalDate fechaNacimiento, LocalDate fechaIngreso) throws Exception {
        setId(id);
        setNombre(nombre);
        setApellido(apellido);
        setDni(dni);
        setFechaNacimiento(fechaNacimiento);
        setFechaIngreso(fechaIngreso);
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

    public void setFechaNacimiento(LocalDate fechaNacimiento) throws Exception {
        if (fechaNacimiento != null) {
            int edad = (int) ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.now());
            if (edad < 18) {
                throw new Exception("El personal debe ser mayor de 18 años");
            }
        }
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

    public abstract double calcularHaberes(double sueldoBase);

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
