package com.grupo14.datos;
import com.grupo14.util.*;

import java.time.LocalDate;
import java.util.*;

public class Pedido {
	private int id;
	private LocalDate fecha;
	private UnidadVenta unidad;
	private Festival festival;
	private Set<DetallePedido> detalles;

	public int getId() {
		return id;
	}

	public Festival getFestival() {
		return festival;
	}

	public void setFestival(Festival festival) {
		this.festival = festival;
	}

	public void setId(int id) throws Exception {
		this.id = validaciones.validarId(id);
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public UnidadVenta getUnidad() {
		return unidad;
	}

	public void setUnidad(UnidadVenta unidad) {
		this.unidad = unidad;
	}

	public Set<DetallePedido> getDetalles() {
		return detalles == null ? new HashSet<>() : detalles;
	}

	public void setDetalles(Set<DetallePedido> detalles) {
		this.detalles = detalles != null ? detalles : new HashSet<>();
	}
	public Pedido(int id, LocalDate fecha, UnidadVenta unidad, Festival festival, Set<DetallePedido> detalles)
			throws Exception {
		if (detalles == null || detalles.isEmpty()) {
			throw new Exception("El detalle no puede estar vacio");
		}
		setId(id);
		setFecha(fecha);
		setDetalles(detalles);
		setUnidad(unidad);
		setFestival(festival);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	public Pedido() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return id == other.id;
	}

	@java.lang.Override
	public java.lang.String toString() {
		return "Pedido{" +
				"id=" + id +
				", fecha=" + fecha +
				", unidad=" + unidad +
				", festival=" + festival +
				", detalles=" + detalles +
				'}';
	}
}
