package com.grupo14.datos;
import com.grupo14.util.*;

public class Plato {
	private int id;
	private String nombre;
	private float precioVenta;
	private float costoProduccion;

	public int getId() {
		return id;
	}

	public void setId(int id) throws Exception {
		validaciones.validarId(id);
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) throws Exception {
		validaciones.validarAtributoString(nombre, "nombre");
		this.nombre = nombre;
	}

	public float getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(float precioVenta) throws Exception {
		validaciones.validarPrecioVenta(precioVenta, getCostoProduccion());
		this.precioVenta = precioVenta;
	}

	public float getCostoProduccion() {
		return costoProduccion;
	}

	public void setCostoProduccion(float costoProduccion) throws Exception {
		validaciones.validarCostoProduccion(costoProduccion);
		this.costoProduccion = costoProduccion;
	}

	public float calcularNeto() {
		return getCostoProduccion() - getPrecioVenta();
	}

	public Plato(String nombre, float precioVenta, float costoProduccion) throws Exception {
		setNombre(nombre);
		setPrecioVenta(precioVenta);
		setCostoProduccion(costoProduccion);
	}

	public Plato() {

	}

	@java.lang.Override
	public java.lang.String toString() {
		return "Plato{" +
				"id=" + id +
				", nombre='" + nombre + '\'' +
				", precioVenta=" + precioVenta +
				", costoProduccion=" + costoProduccion +
				'}';
	}
}
