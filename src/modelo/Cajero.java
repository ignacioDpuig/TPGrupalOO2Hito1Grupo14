package modelo;

import java.time.LocalDate;

public class Cajero extends Personal {
	private String turno;

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) throws Exception {
		this.turno = validaciones.validarTurno(turno);
	}

	@Override
	public double calcularHaberes(ConfiguracionCostos configuracionFestival) {

		return configuracionFestival.getSueldoBase() + calcularAntiguedad() * configuracionFestival.getPlusAntiguedad();
	}

	public Cajero(int id, String nombre, String apellido, long dni, LocalDate fechaNacimiento, LocalDate fechaIngreso,
			String turno) throws Exception {
		super(id, nombre, apellido, dni, fechaNacimiento, fechaIngreso);
		this.turno = turno;
	}

	public Cajero(Cajero otro) throws Exception {
		super(otro);
		this.turno = otro.turno;
	}

	@Override
	public String toString() {
		return "Cajero{" +
				"turno='" + turno + '\'' +
				'}';
	}

	@Override
	public String toTable() {
		return String.format(" %-10d| %-8s |", dni, "Cajero");
	}
}
