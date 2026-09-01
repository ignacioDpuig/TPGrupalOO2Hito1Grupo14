package com.grupo14.datos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DetallePedido other = (DetallePedido) obj;
        return Objects.equals(pedido, other.pedido) && Objects.equals(plato, other.plato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedido.getId(), plato.getId());
    }
}