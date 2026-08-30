package com.grupo14.datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Festival {

    private int id;
    private String nombre;
    private String temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<UnidadVenta> unidades = new ArrayList<>();

    public Festival() {
    }

    public Festival(int id, String nombre, String temporada,
                    LocalDate fechaInicio, LocalDate fechaFin,
                    List<UnidadVenta> unidades) {
        this.id = id;
        this.nombre = nombre;
        this.temporada = temporada;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.unidades = unidades != null ? unidades : new ArrayList<>();
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

    public List<UnidadVenta> getUnidades() { return unidades; }
    public void setUnidades(List<UnidadVenta> unidades) {
        this.unidades = unidades != null ? unidades : new ArrayList<>();
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
