package com.grupo14.datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class UnidadVenta {
    protected int id;
    protected String nombreComercial;
    protected Personal responsableACargo;
    protected float superficie;
    protected String codigo;
    protected List<Personal> staff = new ArrayList<>();
    protected List<Plato> platos = new ArrayList<>();
    protected List<Pedido> pedidos = new ArrayList<>();

    public UnidadVenta() {
    }

    public UnidadVenta(int id, String nombreComercial, Personal responsableACargo,
                       float superficie, String codigo, List<Personal> staff,
                       List<Pedido> pedidos, List<Plato> platos) {
        this.id = id;
        this.nombreComercial = nombreComercial;
        this.responsableACargo = responsableACargo;
        this.superficie = superficie;
        this.codigo = codigo;
        this.staff = staff != null ? staff : new ArrayList<>();
        this.pedidos = pedidos != null ? pedidos : new ArrayList<>();
        this.platos = platos != null ? platos : new ArrayList<>();
    }

    // --- Getters y setters simples ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public Personal getResponsableACargo() { return responsableACargo; }
    public void setResponsableACargo(Personal responsableACargo) { this.responsableACargo = responsableACargo; }

    public float getSuperficie() { return superficie; }
    public void setSuperficie(float superficie) { this.superficie = superficie; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    // --- Listas ---

    public List<Personal> getStaff() { return staff; }
    public void setStaff(List<Personal> staff) { this.staff = staff != null ? staff : new ArrayList<>(); }

    public List<Plato> getPlatos() { return platos; }
    public void setPlatos(List<Plato> platos) { this.platos = platos != null ? platos : new ArrayList<>(); }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos != null ? pedidos : new ArrayList<>(); }

    // --- Métodos de gestión ---

    public boolean agregarPersonal(Personal personal) {
        return staff.add(personal);
    }

    public boolean retirarPersonal(Personal personal) {
        return staff.remove(personal);
    }

    public boolean agregarPlato(Plato plato) {
        return platos.add(plato);
    }

    public boolean quitarPlato(Plato plato) {
        return platos.remove(plato);
    }

    public boolean agregarPedido(Pedido pedido) {
        return pedidos.add(pedido);
    }

    public boolean retirarPedido(Pedido pedido) {
        return pedidos.remove(pedido);
    }

    // --- Métodos de negocio que dependen del Festival / ConfiguracionCostos ---

    public double calcularSueldos(Festival festival) {
        ConfiguracionCostos config = festival.getConfiguracionCostos();
        double sueldos = 0;
        for (Personal personal : staff) {
            sueldos += personal.calcularHaberes(config);
        }
        return sueldos;
    }

    public abstract double calcularCannon(Festival festival);

    public Plato platoEstrella() {
        if (pedidos == null || pedidos.isEmpty()) {
            return null;
        }
        List<DetallePedido> totalesPorPlato = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            for (DetallePedido detalleActual : pedido.getDetalles()) {
                Plato platoActual = detalleActual.getPlato();
                int cantidadActual = detalleActual.getCantidad();
                boolean platoEncontrado = false;
                for (DetallePedido total : totalesPorPlato) {
                    if (total.getPlato().equals(platoActual)) {
                        total.setCantidad(total.getCantidad() + cantidadActual);
                        platoEncontrado = true;
                        break;
                    }
                }
                if (!platoEncontrado) {
                    // Pasamos null en el pedido ya que es un objeto acumulador temporal en memoria
                    totalesPorPlato.add(new DetallePedido(null, platoActual, cantidadActual));
                }
            }
        }
        if (totalesPorPlato.isEmpty()) {
            return null;
        }
        DetallePedido detalleEstrella = totalesPorPlato.get(0);
        for (int i = 1; i < totalesPorPlato.size(); i++) {
            if (totalesPorPlato.get(i).getCantidad() > detalleEstrella.getCantidad()) {
                detalleEstrella = totalesPorPlato.get(i);
            }
        }
        return detalleEstrella.getPlato();
    }

    public double calcularRentabilidadNeta(Festival festival) {
        double gananciaNeta = 0;
        for (Pedido pedido : pedidos) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
            }
        }
        return gananciaNeta - calcularSueldos(festival) - calcularCannon(festival);
    }

    public double calcularRentabilidadNetaEntreFechas(Festival festival, LocalDate fechaDesde, LocalDate fechaHasta) {
        double gananciaNeta = 0;
        for (Pedido pedido : pedidos) {
            if (!pedido.getFecha().isBefore(fechaDesde) && !pedido.getFecha().isAfter(fechaHasta)) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
                }
            }
        }
        return gananciaNeta - calcularSueldos(festival) - calcularCannon(festival);
    }

    public float calcularRecaudacionTotal() {
        float total = 0;
        for (Pedido pedido : pedidos) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                total += detalle.getCantidad() * detalle.getPlato().getPrecioVenta();
            }
        }
        return total;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        UnidadVenta other = (UnidadVenta) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public abstract String toString();
}