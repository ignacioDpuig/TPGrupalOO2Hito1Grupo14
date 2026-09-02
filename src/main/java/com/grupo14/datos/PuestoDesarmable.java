package com.grupo14.datos;

import java.util.Set;

public class PuestoDesarmable extends UnidadVenta {
    private int cantidadCarpas;
    private float tiempoMontajeMinutos;

    public PuestoDesarmable() {
    }

    public PuestoDesarmable(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
                            Set<Personal> staff, int cantidadCarpas, float tiempoMontajeMinutos,
                            Set<Pedido> pedidos, Set<Plato> platos) throws Exception{
        super(id, nombreComercial, responsableACargo, superficie, codigo, staff, pedidos, platos);
        setCantidadCarpas(cantidadCarpas);
        setTiempoMontajeMinutos(tiempoMontajeMinutos);
    }

    public int getCantidadCarpas() {
        return cantidadCarpas;
    }

    public void setCantidadCarpas(int cantidadCarpas) {
        this.cantidadCarpas = cantidadCarpas;
    }

    public float getTiempoMontajeMinutos() {
        return tiempoMontajeMinutos;
    }

    public void setTiempoMontajeMinutos(float tiempoMontajeMinutos) {
        this.tiempoMontajeMinutos = tiempoMontajeMinutos;
    }

    @Override
    public double calcularCannon(Festival festival) {
        double cannon = getSuperficie() * festival.getCostoSuperficie();
        cannon += getTiempoMontajeMinutos() * festival.getCostoMontaje();
        return cannon;
    }

    @Override
    public String toString() {
        return "PuestoDesarmable [cantidadCarpas=" + cantidadCarpas +
               ", tiempoMontajeMinutos=" + tiempoMontajeMinutos + "]";
    }
}