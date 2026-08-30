package com.grupo14.datos;

public class DetallePedido {

    private Pedido pedido;
    private Plato plato;
    private int cantidad;

    public DetallePedido() {}

    public DetallePedido(Pedido pedido, Plato plato, int cantidad) {
        this.pedido = pedido;
        this.plato = plato;
        this.cantidad = cantidad;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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