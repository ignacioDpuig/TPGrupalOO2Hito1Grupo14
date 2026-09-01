package com.grupo14.datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Festival {

    private int id;
    private String nombre;
    private String temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double costoSuperficie;
    private double costoMontaje;
    private double plusElectricidad;
    private double sueldoBase;
    private Set<UnidadVenta> unidades = new HashSet<>();
    public Festival() {
    }

    public Festival(int id, String nombre, String temporada,
                    LocalDate fechaInicio, LocalDate fechaFin,
                    double costoSuperficie, double costoMontaje,
                    double plusElectricidad, double sueldoBase,
                    List<UnidadVenta> unidades) {
        this.id = id;
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoSuperficie = costoSuperficie;
        this.costoMontaje = costoMontaje;
        this.plusElectricidad = plusElectricidad;
        this.sueldoBase = sueldoBase;
        this.unidades = unidades != null ? new HashSet<>(unidades) : new HashSet<>();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    // --- Getters y Setters de ConfiguracionCostos ---
    public double getCostoSuperficie() { return costoSuperficie; }
    public void setCostoSuperficie(double costoSuperficie) { this.costoSuperficie = costoSuperficie; }

    public double getCostoMontaje() { return costoMontaje; }
    public void setCostoMontaje(double costoMontaje) { this.costoMontaje = costoMontaje; }

    public double getPlusElectricidad() { return plusElectricidad; }
    public void setPlusElectricidad(double plusElectricidad) { this.plusElectricidad = plusElectricidad; }

    public double getSueldoBase() { return sueldoBase; }
    public void setSueldoBase(double sueldoBase) { this.sueldoBase = sueldoBase; }

    public Set<UnidadVenta> getUnidades() { return unidades; }
    public void setUnidades(Set<UnidadVenta> unidades) {
        this.unidades = unidades != null ? unidades : new HashSet<>();
    }


    public boolean agregarUnidad(UnidadVenta unidad) {
        if (unidad == null) return false;
        return unidades.add(unidad);
    }

    public boolean eliminarUnidad(UnidadVenta unidad) {
        return unidades.remove(unidad);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Festival other = (Festival) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return "Festival [id=" + id + ", nombre=" + nombre + ", temporada=" + temporada +
               ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]";
    }
}