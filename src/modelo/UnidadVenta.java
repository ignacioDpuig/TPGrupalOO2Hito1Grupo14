package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class UnidadVenta {
	protected int id;
	protected String nombreComercial;
	protected Personal responsableACargo;
	protected float superficie;
	protected String codigo;
	protected List<Personal> staff = new ArrayList<Personal>();
	protected ConfiguracionCostos configuracionCostos;
	protected List<Plato> platos = new ArrayList<Plato>();
	protected List<Pedido> pedidos = new ArrayList<Pedido>();

	public ConfiguracionCostos getConfiguracionCostos() {
		return configuracionCostos;
	}

	public void setConfiguracionCostos(ConfiguracionCostos configuracionCostos) {
		this.configuracionCostos = configuracionCostos;
	}

	public List<Plato> getPlatos() {
		return new ArrayList<>(platos);
	}

	public void setPlatos(List<Plato> platos) {
		if (platos != null) {
			this.platos = platos;
		} else {
			this.platos = new ArrayList<Plato>();
		}
	}

	public boolean agregarPlato(Plato plato) throws Exception {
		plato.setId(Funciones.proximoIdPlato(platos));
		return platos.add(plato);
	}

	public boolean quitarPlato(Plato plato) throws Exception {
		Plato platoABorrar = Funciones.buscarPlatoPorId(id, platos);
		if (Objects.isNull(platoABorrar)) {
			throw new Exception("No se encontro al festival con id: " + id);
		}
		return platos.remove(plato);
	}

	public List<Pedido> getPedidos() {
		return new ArrayList<>(pedidos);
	}

	public void setPedidos(List<Pedido> pedidos) {
		if (pedidos != null) {
			this.pedidos = pedidos;
		} else {
			this.pedidos = new ArrayList<Pedido>();
		}
	}

	public int getId() {
		return id;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) throws Exception {
		this.nombreComercial = validaciones.validarAtributoString(nombreComercial, "nombreComercial");
	}

	public Personal getResponsableACargo() {
		return responsableACargo;
	}

	public void setResponsableACargo(Personal responsableACargo) throws Exception {
		this.responsableACargo = responsableACargo;
		agregarPersonal(responsableACargo);
	}

	public float getSuperficie() {
		return superficie;
	}

	public void setSuperficie(float superficie) throws Exception {
		this.superficie = validaciones.validarSuperficie(superficie);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) throws Exception {
		this.codigo = validaciones.validarCodigoUnidadVenta(codigo, getNombreComercial());
	}

	public List<Personal> getStaff() {
		return new ArrayList<>(staff);
	}

	public void setStaff(List<Personal> staff) {
		if (staff != null) {
			this.staff = staff;
		} else {
			this.staff = new ArrayList<Personal>();
		}
	}

	public double calcularSueldos(ConfiguracionCostos configuracionCostos) {
		float sueldos = 0f;
		for (Personal personal : staff) {
			sueldos += personal.calcularHaberes(configuracionCostos);
		}
		return sueldos;
	}

	public abstract double calcularCannon(ConfiguracionCostos configuracionCostos);

	public boolean agregarPersonal(Personal personal) throws Exception {
		Personal aux = null;
		if (personal instanceof Cocinero) {
			aux = new Cocinero((Cocinero) personal);
		} else if (personal instanceof Cajero) {
			aux = new Cajero((Cajero) personal);
		} else if (personal instanceof Encargado) {
			aux = new Encargado((Encargado) personal);
		}
		return staff.add(aux);
	}

	public boolean retirarPersonal(Personal personal) {
		return staff.remove(personal);
	}

	public boolean agregarPedido(Pedido pedido) {
		return pedidos.add(pedido);
	}

	public boolean retirarPedido(Pedido pedido) {
		return pedidos.remove(pedido);
	}

	public Plato platoEstrella() {
		if (pedidos == null || pedidos.isEmpty()) {
			return null;
		}
		List<DetallePedido> totalesPorPlato = new ArrayList<DetallePedido>();

		for (Pedido pedido : pedidos) {
			for (DetallePedido detalleActual : pedido.getDetalles()) {
				Plato platoActual = detalleActual.getPlato();
				int cantidadActual = detalleActual.getCantidad();
				boolean platoEncontrado = false;
				for (DetallePedido total : totalesPorPlato) {
					if (total.getPlato().equals(platoActual)) {
						total.setCantidad(total.getCantidad() + cantidadActual);
						platoEncontrado = true;
						break;
					}
				}
				if (!platoEncontrado) {
					totalesPorPlato.add(new DetallePedido(platoActual, cantidadActual));
				}
			}
		}
		if (totalesPorPlato.isEmpty()) {
			return null;
		}
		DetallePedido detalleEstrella = totalesPorPlato.get(0);

		for (int i = 1; i < totalesPorPlato.size(); i++) {
			if (totalesPorPlato.get(i).getCantidad() > detalleEstrella.getCantidad()) {
				detalleEstrella = totalesPorPlato.get(i);
			}
		}
		return detalleEstrella.getPlato();
	}

	public double calcularRentabilidadNeta() {
		float gananciaNeta = 0f;
		for (Pedido pedido : getPedidos()) {
			for (DetallePedido detalle : pedido.getDetalles()) {
				gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
			}

		}
		return gananciaNeta - calcularSueldos(getConfiguracionCostos()) - calcularCannon(getConfiguracionCostos());
	}

	public double calcularRentabilidadNetaEntreFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
		float gananciaNeta = 0f;
		for (Pedido pedido : getPedidos()) {
			if (fechaDesde.compareTo(pedido.getFecha()) >= 0 && fechaHasta.compareTo(pedido.getFecha()) <= 0) {
				for (DetallePedido detalle : pedido.getDetalles()) {
					gananciaNeta += detalle.getPlato().calcularNeto() * detalle.getCantidad();
				}

			}
		}
		return gananciaNeta - calcularSueldos(getConfiguracionCostos()) - calcularCannon(getConfiguracionCostos());
	}

	public float calcularRecaudacionTotal() {
		float totalRecaudacion = 0f;
		for (Pedido pedido : getPedidos()) {
			for (DetallePedido detalle : pedido.getDetalles()) {
				totalRecaudacion += detalle.getCantidad() * detalle.getPlato().getPrecioVenta();
			}
		}
		return totalRecaudacion;
	}

	public UnidadVenta(int id, String nombreComercial, Personal responsableACargo, float superficie, String codigo,
			List<Personal> staff, ConfiguracionCostos costos, List<Pedido> pedidos, List<Plato> platos)
			throws Exception {
		this.id = id;
		setNombreComercial(nombreComercial);
		setStaff(staff);
		setResponsableACargo(responsableACargo);
		setSuperficie(superficie);
		setCodigo(codigo);
		setPedidos(pedidos);
		setPlatos(platos);
		setConfiguracionCostos(costos);
	}

	public UnidadVenta(UnidadVenta unidad) throws Exception {
		this.id = unidad.getId();
		setNombreComercial(unidad.getNombreComercial());
		setStaff(unidad.getStaff());
		setResponsableACargo(unidad.getResponsableACargo());
		setSuperficie(unidad.getSuperficie());
		setCodigo(unidad.getCodigo());
		setPedidos(unidad.getPedidos());
		setPlatos(unidad.getPlatos());
		setConfiguracionCostos(unidad.getConfiguracionCostos());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		UnidadVenta other = (UnidadVenta) obj;
		return Objects.equals(codigo, other.codigo);
	}

	@Override
	public abstract String toString();
}
