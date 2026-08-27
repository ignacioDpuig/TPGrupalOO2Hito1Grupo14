package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Festival {

	private int id;
	private String nombre;
	private String temporada;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private List<UnidadVenta> unidades = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) throws Exception {
		this.id = validaciones.validarId(id);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) throws Exception {
		this.nombre = validaciones.validarAtributoString(nombre, "nombre");
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) throws Exception {
		this.temporada = validaciones.validarAtributoString(temporada, "temporada");
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) throws Exception {
		this.fechaInicio = validaciones.validarFecha(fechaInicio);
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) throws Exception {
		this.fechaFin = validaciones.validarFecha(fechaFin);
	}

	public List<UnidadVenta> getUnidades() {
		return new ArrayList<>(unidades);
	}

	public void setUnidades(List<UnidadVenta> unidades) {
		if (unidades == null) {
			this.unidades = new ArrayList<UnidadVenta>();
		}
		this.unidades = unidades;
	}

	public boolean agregarUnidad(UnidadVenta unidad) {
		if (unidad == null) {
			return false;
		}
		return unidades.add(unidad);
	}

	public boolean eliminarUnidad(UnidadVenta unidad) {
		return unidades.remove(unidad);
	}

	public UnidadVenta buscarUnidadVentaPorCodigo(String codigo) {
		return Funciones.buscarUnidadVentaPorCodigo(codigo, unidades);

	}

	public Festival(int id, String nombre, String temporada, LocalDate fechaInicio, LocalDate fechaFin,
			List<UnidadVenta> unidades) throws Exception {
		setId(id);
		setNombre(nombre);
		setTemporada(temporada);
		setFechaInicio(fechaInicio);
		setFechaFin(fechaFin);
		setUnidades(unidades);
	}

	public Festival() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Festival other = (Festival) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Festival{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				", temporada='" + temporada + '\'' +
				", fechaInicio=" + fechaInicio +
				", fechaFin=" + fechaFin +
				", unidades=" + unidades +
				'}';
	}
}
