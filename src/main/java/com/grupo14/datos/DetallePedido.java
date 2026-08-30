package com.grupo14.datos;

public class DetallePedido {

    
    private Plato plato;
    private int cantidad;

  	public DetallePedido() {}

    public DetallePedido(Plato plato, int cantidad) {
        this.plato = plato;
        this.cantidad = cantidad;
    }


    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}