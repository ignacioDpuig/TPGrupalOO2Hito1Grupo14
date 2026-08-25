package modelo;

import java.time.LocalDate;

public class Encargado extends Personal {
	private int personalACargo;

	public int getPersonalACargo() {
		return personalACargo;
	}

	public void setPersonalACargo(int personalACargo) {
		this.personalACargo = personalACargo;
	}

	public Encargado(int id, String nombre, String apellido, long dni, LocalDate fechaNacimiento,
			LocalDate fechaIngreso, int personalACargo) throws Exception {
		super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
		this.personalACargo = personalACargo;
	}

	public Encargado(Encargado otro) throws Exception {
		super(otro);
		this.personalACargo = otro.personalACargo;
	}

	public Encargado() {
		super();
	}

	@Override
	public String toString() {
		return String.format("| %-4d | %-20s | %-10d | %-10s | %-10s | %-4d | %-11s | %-4s | %-10s |", id,
				nombre + " " + apellido, dni, fechaNacimiento, fechaIngreso, personalACargo, "x", "x", "x");
	}

	@Override
	public double calcularHaberes(ConfiguracionCostos configuracionFestival) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toTable() {
		return String.format(" %-10d| %-8s|", dni, "Encargado");
	}
}
