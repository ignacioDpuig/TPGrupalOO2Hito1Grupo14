package modelo;

import java.util.ArrayList;
import java.util.List;

public class Funciones {
	public static int proximoIdPersonal(List<Personal> staff) {
		if (staff.isEmpty())
			return 1;
		return staff.getLast().getId() + 1;
	}

	public static int proximoIdStaff(List<Personal> staff) {
		if (staff == null || staff.isEmpty())
			return 1;
		return staff.getLast().getId() + 1;
	}

	public static int proximoIdUnidadVenta(List<UnidadVenta> unidades) {
		if (unidades.isEmpty())
			return 1;
		return unidades.getLast().getId() + 1;
	}

	public static int proximoIdFestivales(List<Festival> festivales) {
		if (festivales.isEmpty())
			return 1;
		return festivales.getLast().getId() + 1;
	}

	public static int proximoIdPedido(List<Pedido> pedidos) {
		if (pedidos.isEmpty())
			return 1;
		return pedidos.getLast().getId() + 1;
	}

	public static int proximoIdPlato(List<Plato> platos) {
		if (platos.isEmpty())
			return 1;
		return platos.getLast().getId() + 1;
	}

	public static Festival buscarFestivalPorId(int id, List<Festival> festivales) throws Exception {
		Festival festivalEncontrado = null;
		Festival festivalBuscado = new Festival();
		festivalBuscado.setId(id);
		int i = 0;
		while (festivalEncontrado == null && i < festivales.size()) {
			Festival festivalActual = festivales.get(i);
			if (festivalActual.equals(festivalBuscado)) {
				festivalEncontrado = festivalActual;
			}
			i++;
		}
		return festivalEncontrado;
	}

	public static Pedido buscarPedidoPorId(int id, List<Pedido> pedidos) throws Exception {
		Pedido pedidoEncontrado = null;
		Pedido pedidoBuscado = new Pedido();
		pedidoBuscado.setId(id);
		int i = 0;
		while (pedidoEncontrado == null && i < pedidos.size()) {
			Pedido pedidoActual = pedidos.get(i);
			if (pedidoActual.equals(pedidoBuscado)) {
				pedidoEncontrado = pedidoActual;
			}
			i++;
		}
		return pedidoEncontrado;
	}

	public static Plato buscarPlatoPorId(int id, List<Plato> platos) throws Exception {
		Plato platoEncontrado = null;
		Plato platoBuscado = new Plato();
		platoBuscado.setId(id);
		;
		int i = 0;
		while (platoEncontrado == null && i < platos.size()) {
			Plato platoActual = platos.get(i);
			if (platoActual.equals(platoBuscado)) {
				platoEncontrado = platoActual;
			}
			i++;
		}
		return platoEncontrado;
	}

	public static Personal buscarPersonalPorDni(long dni, List<Personal> staff) throws Exception {
		Personal personalEncontrado = null;
		Personal personalBuscado = new Encargado();
		personalBuscado.setDni(dni);
		int i = 0;
		while (personalEncontrado == null && i < staff.size()) {
			Personal personalActual = staff.get(i);
			if (personalBuscado.equals(personalActual)) {
				personalEncontrado = personalActual;
			}
			i++;
		}
		return personalEncontrado;
	}

	public static UnidadVenta buscarUnidadVentaPorCodigo(String codigo, List<UnidadVenta> unidades) {
		UnidadVenta unidadEncontrada = null;
		int i = 0;
		while (unidadEncontrada == null && i < unidades.size()) {
			UnidadVenta unidadActual = unidades.get(i);
			if (unidadActual.getCodigo().equals(codigo)) {
				unidadEncontrada = unidadActual;
			}
			i++;

		}
		return unidadEncontrada;
	}

}
