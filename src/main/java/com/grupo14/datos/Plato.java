package com.grupo14.datos;

public class Plato {
    private int id;
    private String nombre;
    private float precioVenta;
    private float costoProduccion;

    public Plato() {
    }

    public Plato(String nombre, float precioVenta, float costoProduccion) {
        setNombre(nombre);
        setPrecioVenta(precioVenta);
        setCostoProduccion(costoProduccion);
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

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public float getCostoProduccion() {
        return costoProduccion;
    }

    public void setCostoProduccion(float costoProduccion) {
        this.costoProduccion = costoProduccion;
    }

    public float calcularNeto() {
        return getPrecioVenta() - getCostoProduccion();
    }

    @Override
    public String toString() {
        return "Plato [id=" + id + ", nombre=" + nombre +
               ", precioVenta=" + precioVenta + ", costoProduccion=" + costoProduccion + "]";
    }
}