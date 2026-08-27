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
		return "Encargado{" +
				"personalACargo=" + personalACargo +
				'}';
	}

	@Override
	public double calcularHaberes(ConfiguracionCostos configuracionFestival) {
		return 0;
	}

	@Override
	public String toTable() {
		return String.format(" %-10d| %-8s|", dni, "Encargado");
	}
}
