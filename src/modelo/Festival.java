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

	public List<ReporteMayoresCanon> calcularMayoresCanon() {
		List<ReporteMayoresCanon> reportes = new ArrayList<>();

		for (UnidadVenta unidad : this.unidades) {
			String tipoUnidad = (unidad instanceof FoodTruck) ? "Food Truck" : "Puesto Desarmable";

			double canon = unidad.calcularCannon(unidad.getConfiguracionCostos());

			reportes.add(new ReporteMayoresCanon(unidad.getNombreComercial(), unidad.getCodigo(), tipoUnidad, canon));
		}

		reportes.sort((r1, r2) -> Double.compare(r2.getCanon(), r1.getCanon()));

		return reportes.subList(0, Math.min(3, reportes.size()));
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
		// Encabezado del festival
		StringBuilder sb = new StringBuilder();
		sb.append("===============================================================\n");
		sb.append("Festival: ").append(nombre).append(" (ID: ").append(id).append(")\n");
		sb.append("Temporada: ").append(temporada).append("\n");
		sb.append("Fechas: ").append(fechaInicio).append(" al ").append(fechaFin).append("\n");
		sb.append("---------------------------------------------------------------\n");
		sb.append("Unidades de Venta:\n");
		sb.append("---------------------------------------------------------------\n");

		// Encabezados de la tabla de unidades
		sb.append(String.format("| %-4s | %-16s | %-10s | %-20s |%n", "ID", "Nombre Comercial", "Superficie", "Staff"));
		sb.append(String.format("| %-4s | %-16s | %-10s |%10s | %8s |%n", "", "", "", "DNI", "CARGO"));

		sb.append("|------|------------------|------------|----------------------|\n");

		// Filas de la tabla (cada unidad de venta)
		for (UnidadVenta unidad : unidades) {
			sb.append(unidad.toString());
			boolean flag = true;
			for (Personal staff : unidad.getStaff()) {
				if (!flag) {
					sb.append(String.format("| %-4s | %-16s | %-10s |%10s|%n", "", "", "", staff.toTable()));
				}
				if (flag) {
					sb.append(String.format("%10s%n", staff.toTable()));
					flag = false;
				}
			}
			sb.append("--------------------------------------------------------------|\n");

		}

		return sb.toString();
	}

}
