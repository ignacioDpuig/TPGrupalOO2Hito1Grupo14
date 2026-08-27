package modelo;

import java.util.List;

public class PuestoDesarmable extends UnidadVenta {
	private int cantidadCarpas;
	private float tiempoMontajeMinutos;

	public int getCantidadCarpas() {
		return cantidadCarpas;
	}

	public void setCantidadCarpas(int cantidadCarpas) {
		this.cantidadCarpas = cantidadCarpas;
	}

	public float getTiempoMontajeMinutos() {
		return tiempoMontajeMinutos;
	}

	public void setTiempoMontajeMinutos(float tiempoMontajeMinutos) {
		this.tiempoMontajeMinutos = tiempoMontajeMinutos;
	}

	@Override
	public double calcularCannon(ConfiguracionCostos configuracionCostos) {
		float cannon = 0f;
		cannon += getSuperficie() * configuracionCostos.getCostoSuperficie();
		cannon -= getTiempoMontajeMinutos() * configuracionCostos.getCostoMontaje();// TODO ESTO DEBERIA SER LOCALDATE
																					// SACAR MIN CALCULAR EL DECIMAL
		return cannon;
	}

	public PuestoDesarmable(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
			List<Personal> staff, int cantidadCarpas, float tiempoMontajeMinutos,
			ConfiguracionCostos configuracionCostos, List<Pedido> pedidos, List<Plato> platos) throws Exception {
		super(id, nombreComercial, responsableACargo, superficie, codigo, staff, configuracionCostos, pedidos, platos);
		this.cantidadCarpas = cantidadCarpas;
		this.tiempoMontajeMinutos = tiempoMontajeMinutos;
	}

	public PuestoDesarmable(PuestoDesarmable unidad) throws Exception {
		super(unidad);
		setCantidadCarpas(unidad.getCantidadCarpas());
		setTiempoMontajeMinutos(unidad.getTiempoMontajeMinutos());
	}

	@java.lang.Override
	public java.lang.String toString() {
		return "PuestoDesarmable{" +
				"cantidadCarpas=" + cantidadCarpas +
				", tiempoMontajeMinutos=" + tiempoMontajeMinutos +
				'}';
	}
}
