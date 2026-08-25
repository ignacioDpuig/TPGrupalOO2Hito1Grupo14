package modelo;

public class ConfiguracionCostos {
	private int edadMinimaPersonal;
	private double costoSuperficie;
	private double costoMontaje;
	private double plusElectricidad;
	private double plusAntiguedad;
	private double sueldoBase;
	private double plusCategoria;

	public int getEdadMinimaPersonal() {
		return edadMinimaPersonal;
	}

	public void setEdadMinimaPersonal(int edadMinimaPersonal) {
		this.edadMinimaPersonal = edadMinimaPersonal;
	}

	public double getCostoSuperficie() {
		return costoSuperficie;
	}

	public void setCostoSuperficie(double costoSuperficie) {
		this.costoSuperficie = costoSuperficie;
	}

	public double getCostoMontaje() {
		return costoMontaje;
	}

	public void setCostoMontaje(double costoMontaje) {
		this.costoMontaje = costoMontaje;
	}

	public double getPlusElectricidad() {
		return plusElectricidad;
	}

	public void setPlusElectricidad(double plusElectricidad) {
		this.plusElectricidad = plusElectricidad;
	}

	public double getPlusAntiguedad() {
		return plusAntiguedad;
	}

	public void setPlusAntiguedad(double plusAntiguedad) {
		this.plusAntiguedad = plusAntiguedad;
	}

	public double getSueldoBase() {
		return sueldoBase;
	}

	public void setSueldoBase(double sueldoBase) {
		this.sueldoBase = sueldoBase;
	}

	public double getPlusCategoria() {
		return plusCategoria;
	}

	public void setPlusCategoria(double plusCategoria) {
		this.plusCategoria = plusCategoria;
	}

	public ConfiguracionCostos(int edadMinimaPersonal, double costoSuperficie, double costoMontaje,
			double plusElectricidad, double plusAntiguedad, double sueldoBase, double plusCategoria) {
		this.edadMinimaPersonal = edadMinimaPersonal;
		this.costoSuperficie = costoSuperficie;
		this.costoMontaje = costoMontaje;
		this.plusElectricidad = plusElectricidad;
		this.plusAntiguedad = plusAntiguedad;
		this.sueldoBase = sueldoBase;
		this.plusCategoria = plusCategoria;
	}
}
