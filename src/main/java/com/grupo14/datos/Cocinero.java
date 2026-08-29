package com.grupo14.datos;
import com.grupo14.util.*;

import java.time.LocalDate;

public class Cocinero extends Personal {
	private String especialidad;
	private int categoria;

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) throws Exception {
		this.especialidad = validaciones.validarAtributoString(especialidad, "especialidad");
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) throws Exception {
		this.categoria = validaciones.validarCategoria(categoria);
	}

	@Override
	public double calcularHaberes(ConfiguracionCostos configuracionFestival) {
		return configuracionFestival.getSueldoBase() + configuracionFestival.getPlusCategoria() * getCategoria();

	}

	public Cocinero(int id, String nombre, String apellido, long dni, LocalDate fechaNacimiento, LocalDate fechaIngreso,
			String especialidad, int categoria) throws Exception {
		super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
		setEspecialidad(especialidad);
		setCategoria(categoria);
	}

	public Cocinero(Cocinero otro) throws Exception {
		super(otro);
		setEspecialidad(otro.especialidad);
		setCategoria(otro.categoria);
	}

	@Override
	public String toString() {
		return "Cocinero{" +
				"especialidad='" + especialidad + '\'' +
				", categoria=" + categoria +
				'}';
	}

	@Override
	public String toTable() {
		return String.format(" %-10d| %-8s ", dni, "Cocinero");
	}
}
