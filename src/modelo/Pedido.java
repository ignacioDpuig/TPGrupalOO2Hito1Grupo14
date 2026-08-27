package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido {
	private int id;
	private LocalDate fecha;
	private UnidadVenta unidad;
	private Festival festival;
	private List<DetallePedido> detalles;

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

	public List<DetallePedido> getDetalles() {
		return new ArrayList<>(detalles);
	}

	public void setDetalles(List<DetallePedido> detalles) throws Exception {
		if (detalles.isEmpty() || detalles == null) {
			throw new Exception("El detalle no puede estar vacio");
		}
		this.detalles = detalles;
	}

	public Pedido(int id, LocalDate fecha, UnidadVenta unidad, Festival festival, List<DetallePedido> detalles)
			throws Exception {
		setId(id);
		setFecha(fecha);
		this.unidad = unidad;
		setDetalles(detalles);
		setFestival(festival);
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
