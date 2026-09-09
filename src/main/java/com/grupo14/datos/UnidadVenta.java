package com.grupo14.datos;

import com.grupo14.util.validaciones;

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
    protected Festival festival;
    public UnidadVenta() {
    }

    public UnidadVenta(int id, String nombreComercial, Personal responsableACargo,
                       float superficie, Set<Personal> staff,
                       Set<Pedido> pedidos, Set<Plato> platos, Festival festival) throws Exception {
        setId(id);
        setNombreComercial(nombreComercial);
        setStaff(staff);
        asignarResponsableACargo(responsableACargo);
        setSuperficie(superficie);
        setCodigo(validaciones.generarCodigoUnidadVenta(this));
        setPedidos(pedidos);
        setPlatos(platos);
        setFestival(festival);
    }

    // --- Getters y setters simples ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public Personal getResponsableACargo() { return responsableACargo; }
    public void setResponsableACargo(Personal responsableACargo) {
        this.responsableACargo = responsableACargo;
    }
    public float getSuperficie() { return superficie; }
    public void setSuperficie(float superficie) { this.superficie = superficie; }

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.length() != 10) {
            throw new Exception("El código debe tener exactamente 10 caracteres");
        }
        this.codigo = codigo;
    }
    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
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

    public void asignarResponsableACargo(Personal responsableACargo) {
        if (responsableACargo == null) {
            throw new IllegalArgumentException("El responsable no puede ser null");
        }
        if (!staff.contains(responsableACargo)) {
            throw new IllegalArgumentException("El responsable debe formar parte del staff");
        }
        this.responsableACargo = responsableACargo;
    }

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

    // --- Métodos de negocio que dependen del Festival  ---

    public double calcularSueldos() {
        double sueldos = 0;
        for (Personal personal : staff) {
            sueldos += personal.calcularHaberes(this.festival.getSueldoBase());
        }
        return sueldos;
    }

    public abstract double calcularCannon();

    public Plato platoEstrella() {
        if (pedidos == null || pedidos.isEmpty()) {
            return null;
        }
        Set<DetallePedido> totalesPorPlato = new HashSet<>();

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
                    totalesPorPlato.add(new DetallePedido(null, platoActual, cantidadActual));
                }
            }
        }
        if (totalesPorPlato.isEmpty()) {
            return null;
        }
        DetallePedido detalleEstrella = null;
        for (DetallePedido detalle : totalesPorPlato) {
            if (detalleEstrella == null || detalle.getCantidad() > detalleEstrella.getCantidad()) {
                detalleEstrella = detalle;
            }
        }

        return detalleEstrella == null ? null : detalleEstrella.getPlato();
    }

    public double calcularRentabilidadNeta(Festival festival) {
        double gananciaNeta = 0;
        for (Pedido pedido : pedidos) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
            }
        }
        return gananciaNeta - calcularSueldos() - calcularCannon();
    }

    public double calcularRentabilidadNetaEntreFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        double gananciaNeta = 0;
        for (Pedido pedido : pedidos) {
            if (!pedido.getFecha().isBefore(fechaDesde) && !pedido.getFecha().isAfter(fechaHasta)) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
                }
            }
        }
        return gananciaNeta - calcularSueldos() - calcularCannon();
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