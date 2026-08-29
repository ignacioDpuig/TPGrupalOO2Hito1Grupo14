package com.grupo14.datos;
import com.grupo14.util.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Personal {
	protected int id;
	protected String nombre;
	protected String apellido;
	protected long dni;
	protected LocalDate fechaNacimiento;
	protected LocalDate fechaIngreso;

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) throws Exception {
		this.nombre = validaciones.validarAtributoString(nombre, "nombre");
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) throws Exception {
		this.apellido = validaciones.validarAtributoString(apellido, "apellido");
		;
	}

	public long getDni() {
		return dni;
	}

	private void setId(int id) throws Exception {
		this.id = validaciones.validarId(id);
		;
	}

	public void setDni(long dni) throws Exception {
		this.dni = validaciones.validarDni(dni);
		;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) throws Exception {
		this.fechaNacimiento = validaciones.validarFechaNacimiento(fechaNacimiento);
		;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public int calcularAntiguedad() {
		LocalDate fechaIngreso = getFechaIngreso();
		LocalDate fechaActual = LocalDate.now();
		int antiguedad = (int) ChronoUnit.YEARS.between(fechaIngreso, fechaActual);
		return antiguedad;
	}

	public abstract double calcularHaberes(ConfiguracionCostos configuracionFestival);

	public Personal(int id, String nombre, String apellido, long dni, LocalDate fechaNacimiento, LocalDate fechaIngreso)
			throws Exception {
		setId(id);
		setNombre(nombre);
		setApellido(apellido);
		setDni(dni);
		setFechaNacimiento(fechaNacimiento);
		setFechaIngreso(fechaIngreso);
	}

	public Personal(Personal personal) throws Exception {
		setId(personal.getId());
		setNombre(personal.getNombre());
		setApellido(personal.getApellido());
		setDni(personal.getDni());
		setFechaNacimiento(personal.getFechaNacimiento());
		setFechaIngreso(personal.getFechaIngreso());
	}

	public Personal() {
	}

	@Override
	public abstract String toString();

	public abstract String toTable();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Personal other = (Personal) obj;
		return dni == other.dni;
	}

}
