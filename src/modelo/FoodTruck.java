package modelo;

import java.util.List;

public class FoodTruck extends UnidadVenta {
	private String patente;
	private boolean requiereConexion;

	public String getPatente() {
		return patente;
	}

	public boolean getRequiereConexion() {
		return requiereConexion;
	}

	public void setPatente(String patente) throws Exception {
		this.patente = validaciones.validarPatente(patente);
	}

	public void setRequiereConexion(boolean requiereConexion) {
		this.requiereConexion = requiereConexion;
	}

	@Override
	public double calcularCannon(ConfiguracionCostos configuracionCostos) {
		float cannon = 0f;
		cannon += getSuperficie() * configuracionCostos.getCostoSuperficie();
		if (getRequiereConexion()) {
			cannon += configuracionCostos.getPlusElectricidad();
		}
		return cannon;
	}

	public FoodTruck(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
			List<Personal> staff, String patente, boolean requiereConexion, ConfiguracionCostos configuracionCostos,
			List<Pedido> pedidos, List<Plato> platos) throws Exception {
		super(id, nombreComercial, responsableACargo, superficie, codigo, staff, configuracionCostos, pedidos, platos);
		setPatente(patente);
		this.requiereConexion = requiereConexion;
	}

	public FoodTruck(FoodTruck unidad) throws Exception {
		super(unidad);
		setPatente(unidad.getPatente());
		setRequiereConexion(unidad.getRequiereConexion());
	}

	@Override
	public String toString() {
		return "FoodTruck{" +
				"patente='" + patente + '\'' +
				", requiereConexion=" + requiereConexion +
				'}';
	}
}
