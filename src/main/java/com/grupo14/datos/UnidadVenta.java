package com.grupo14.datos;

import java.time.LocalDate;
import java.util.*;

public abstract class UnidadVenta {
    protected int id;
    protected String nombreComercial;
    protected Personal responsableACargo;
    protected float superficie;
    protected String codigo;
    protected Set<Personal> staff = new HashSet<>();
    protected Set<Plato> platos = new HashSet<>();
    protected Set<Pedido> pedidos = new HashSet<>();

    public UnidadVenta() {
    }

    public UnidadVenta(int id, String nombreComercial, Personal responsableACargo,
                       float superficie, String codigo, Set<Personal> staff,
                       Set<Pedido> pedidos, Set<Plato> platos) throws Exception {
        setId(id);
        setNombreComercial(nombreComercial);
        setResponsableACargo(responsableACargo);
        setSuperficie(superficie);
        setCodigo(codigo);
        setStaff(staff);
        setPedidos(pedidos);
        setPlatos(platos);
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

    public void setCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.length() != 10) {
            throw new Exception("El código debe tener exactamente 10 caracteres");
        }
        this.codigo = codigo;
    }
    // --- Listas ---

    public Set<Personal> getStaff() { return staff; }
    public void setStaff(Set<Personal> staff) {
        this.staff = staff != null ? staff : new HashSet<>();
    }

    public Set<Plato> getPlatos() { return platos; }
    public void setPlatos(Set<Plato> platos) {
        this.platos = platos != null ? platos : new HashSet<>();
    }
    public Set<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos != null ? pedidos : new HashSet<>();
    }
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
        double sueldos = 0;
        for (Personal personal : staff) {
            sueldos += personal.calcularHaberes(festival.getSueldoBase());
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