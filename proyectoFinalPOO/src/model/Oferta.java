package model;

import util.EstadoOferta;

import java.util.Date;

public class Oferta {
	
	private String idOferta;
	private String idPublicacion;
	private String idOfertante;
	private Date fechaOferta;
	
	private double montoOferta; // Monto en dinero ofrecido
	private String descripcionTrueque; // Descripción del trueque ofrecido
	
	public Oferta(String idOferta, String idPublicacion, String idOfertante, Date fechaOferta, double montoOferta, String descripcionTrueque) {
		this.idOferta = idOferta;
		this.idPublicacion = idPublicacion;
		this.idOfertante = idOfertante;
		this.fechaOferta = fechaOferta;
		this.montoOferta = montoOferta;
		this.descripcionTrueque = descripcionTrueque;
	}

	public String getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(String idOferta) {
		this.idOferta = idOferta;
	}

	public String getIdPublicacion() {
		return idPublicacion;
	}

	public void setIdPublicacion(String idPublicacion) {
		this.idPublicacion = idPublicacion;
	}

	public String getIdOfertante() {
		return idOfertante;
	}

	public void setIdOfertante(String idOfertante) {
		this.idOfertante = idOfertante;
	}

	public Date getFechaOferta() {
		return fechaOferta;
	}

	public void setFechaOferta(Date fechaOferta) {
		this.fechaOferta = fechaOferta;
	}

	public double getMontoOferta() {
		return montoOferta;
	}

	public void setMontoOferta(double montoOferta) {
		this.montoOferta = montoOferta;
	}

	public String getDescripcionTrueque() {
		return descripcionTrueque;
	}

	public void setDescripcionTrueque(String descripcionTrueque) {
		this.descripcionTrueque = descripcionTrueque;
	}
	
	// Getters y setters
	
}
