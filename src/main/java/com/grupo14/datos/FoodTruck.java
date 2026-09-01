package com.grupo14.datos;

import java.util.Set;

public class FoodTruck extends UnidadVenta {
    private String patente;
    private boolean requiereConexion;

    public FoodTruck() {
    }

    public FoodTruck(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
                     Set<Personal> staff, String patente, boolean requiereConexion,
                     Set<Pedido> pedidos, Set<Plato> platos) {
        super(id, nombreComercial, responsableACargo, superficie, codigo, staff, pedidos, platos);
        this.patente = patente;
        this.requiereConexion = requiereConexion;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public boolean getRequiereConexion() {
        return requiereConexion;
    }

    public void setRequiereConexion(boolean requiereConexion) {
        this.requiereConexion = requiereConexion;
    }

    @Override
    public double calcularCannon(Festival festival) {
        double cannon = getSuperficie() * festival.getCostoSuperficie();
        if (getRequiereConexion()) {
            cannon += festival.getPlusElectricidad();
        }
        return cannon;
    }

    @Override
    public String toString() {
        return "FoodTruck [patente=" + patente + ", requiereConexion=" + requiereConexion + "]";
    }
}