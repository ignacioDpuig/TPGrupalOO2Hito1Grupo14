package com.grupo14.datos;

import java.util.List;

public class PuestoDesarmable extends UnidadVenta {
    private int cantidadCarpas;
    private float tiempoMontajeMinutos;

    public PuestoDesarmable() {
    }

    public PuestoDesarmable(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
                            List<Personal> staff, int cantidadCarpas, float tiempoMontajeMinutos,
                            ConfiguracionCostos configuracionCostos, List<Pedido> pedidos, List<Plato> platos) {
        super(id, nombreComercial, responsableACargo, superficie, codigo, staff, configuracionCostos, pedidos, platos);
        this.cantidadCarpas = cantidadCarpas;
        this.tiempoMontajeMinutos = tiempoMontajeMinutos;
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
    public double calcularCannon(ConfiguracionCostos configuracionCostos) {
        double cannon = getSuperficie() * configuracionCostos.getCostoSuperficie();
        cannon -= getTiempoMontajeMinutos() * configuracionCostos.getCostoMontaje();
        // TODO: esto debería usar LocalDate, sacar minutos y calcular el decimal
        return cannon;
    }

    @Override
    public String toString() {
        return "PuestoDesarmable [cantidadCarpas=" + cantidadCarpas +
               ", tiempoMontajeMinutos=" + tiempoMontajeMinutos + "]";
    }
}
