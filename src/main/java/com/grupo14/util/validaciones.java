package com.grupo14.util;

import com.grupo14.datos.UnidadVenta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.Period;
import java.util.Arrays;

public class validaciones {
	public static String validarAtributoString(String atributo, String nombreAtributo) throws Exception {
		if (atributo.isBlank() || atributo == null) {
			throw new Exception("El atributo " + nombreAtributo + " no puede estar vacio");
		}
		return atributo;
	}

	public static LocalDate validarFechaNacimiento(LocalDate fechaNacimiento) throws Exception {
		if (fechaNacimiento == null) {
			throw new Exception("El atributo fechaNacimiento no puede estar vacio");
		}
		Period periodo = Period.between(fechaNacimiento, LocalDate.now());
		if (periodo.getYears() < constantes.EDAD_MINIMA_PERSONAl) {
			throw new Exception("El Personal debe ser mayor de edad.");
		}
		return fechaNacimiento;
	}

	public static long validarDni(long dni) throws Exception {
		if (dni < constantes.MINIMO_DNI_PERMITIDO || dni > constantes.MAXIMO_DNI_PERMITIDO) {
			throw new Exception("Dni Invalido. El rango permitido es desde " + constantes.MINIMO_DNI_PERMITIDO
					+ " hasta " + constantes.MAXIMO_DNI_PERMITIDO);
		}
		return dni;
	}

	public static int validarId(int id) throws Exception {
		if (id < constantes.MENOR_ID_PERMITIDO) {
			throw new Exception("El id debe ser mayor a " + constantes.MENOR_ID_PERMITIDO);
		}
		return id;
	}

	public static float validarCostoProduccion(float costoProduccion) throws Exception {
		if (costoProduccion < 0) {
			throw new Exception("El costoProduccion debe ser mayor a 0.");
		}
		return costoProduccion;
	}

	public static float validarPrecioVenta(float precioVenta, float costoProduccion) throws Exception {
		if (precioVenta <= 0 || costoProduccion > precioVenta) {
			throw new Exception("El precioVenta debe ser mayor a 0 y al costo de produccion.");
		}
		return precioVenta;
	}

	public static float validarSuperficie(float superficie) throws Exception {
		if (superficie <= 0) {
			throw new Exception("La superficie debe ser mayor a 0");
		}
		return superficie;
	}

	public static String validarTurno(String turnoStr) throws Exception {
		if (turnoStr == null || turnoStr.trim().isEmpty()) {
			throw new Exception("El turno no puede ser nulo o vacío.");
		}
		String turnoEnMayusculas = turnoStr.toUpperCase();
		if (Arrays.asList(constantes.TURNOS_PERMITIDOS).contains(turnoEnMayusculas)) {
			return turnoEnMayusculas;
		} else {
			String turnosValidos = String.join(", ", constantes.TURNOS_PERMITIDOS);
			throw new Exception("El turno '" + turnoStr + "' no es válido. Use: " + turnosValidos + ".");
		}
	}

	public static int validarCategoria(int categoria) throws Exception {
		for (int categoriaPermitida : constantes.CATEGORIAS_PERMITIDAS) {
			if (categoriaPermitida == categoria) {
				return categoria;
			}
		}
		String categoriasStr = Arrays.toString(constantes.CATEGORIAS_PERMITIDAS);
		throw new Exception(
				"La categoría '" + categoria + "' no es válida. Use una de las siguientes: " + categoriasStr + ".");
	}

	public static String generarCodigoUnidadVenta(UnidadVenta unidad) throws Exception {
		if (unidad == null) {
			throw new Exception("La unidad no puede ser nula.");
		}
		if (unidad.getNombreComercial() == null || unidad.getNombreComercial().isBlank()) {
			throw new Exception("La unidad debe tener nombre comercial.");
		}
		if (unidad.getId() < 0 || unidad.getId() > 9999999) {
			throw new Exception("El id debe estar entre 0 y 9999999.");
		}

		String nombre = unidad.getNombreComercial()
				.replaceAll("[^A-Za-z]", "")
				.toUpperCase();

		if (nombre.length() >= 3) {
			nombre = nombre.substring(0, 3);
		} else {
			nombre = String.format("%-3s", nombre).replace(' ', 'X');
		}

		String id = String.format("%07d", unidad.getId());

		return nombre + id;
	}

	public static String validarPatente(String patenteStr) throws Exception {
		if (patenteStr == null || patenteStr.trim().isEmpty()) {
			throw new Exception("La patente no puede ser nula o vacía.");
		}
		String patente = patenteStr.toUpperCase();
		if (patente.length() == 6) {
			// Formato viejo: 3 letras y 3 números (LLLNNN)
			if (patente.matches("^[A-Z]{3}[0-9]{3}$")) {
				return patente;
			} else {
				throw new Exception(
						"Formato inválido para patente de 6 caracteres. Se esperaba LLLNNN (3 letras seguidas de 3 números).");
			}
		} else if (patente.length() == 7) {
			// Formato nuevo: 2 letras, 3 números, 2 letras (LLNNNLL)
			if (patente.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$")) {
				return patente;
			} else {
				throw new Exception(
						"Formato inválido para patente de 7 caracteres. Se esperaba LLNNNLL (2 letras, 3 números, 2 letras).");
			}
		} else {
			throw new Exception(
					"La patente '" + patenteStr + "' tiene una longitud inválida. Debe tener 6 o 7 caracteres.");
		}
	}

	public static LocalDate validarFecha(LocalDate fechaHasta) throws Exception {
		if (fechaHasta.isBefore(LocalDate.now())) {// fecha es antes que hoy (se usa para fechas desde/hasta)
			throw new Exception("La fecha no puede ser anterior a la fecha actual");
		}
		return fechaHasta;
	}

}
