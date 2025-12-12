/**
 * Clase: PublicacionSubasta
 * Modelo de publicación tipo subasta.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.0
 */

package model;

import java.util.Date;
import java.util.List;

import util.TipoPublicacion;

public class PublicacionSubasta extends Publicacion {
	
	private double precioMinimo;
	private Date fechaCierre;
	
	 /**
     * Crea una nueva publicación de subasta.
     *
     * @param idArticulo       Identificador único del artículo subastado.
     * @param titulo           Título de la publicación.
     * @param descripcion      Descripción del artículo.
     * @param idVendedor       ID del usuario que publica la subasta.
     * @param fotosPaths       Lista de rutas a las imágenes asociadas.
     * @param precioMinimo     Precio mínimo para iniciar la subasta.
     * @param fechaVencimiento Parámetro no utilizado (verificar si se debe eliminar).
     * @param fechaCierre      Fecha límite para recibir ofertas.
     */
	public PublicacionSubasta(String idArticulo, String titulo, String descripcion, 
            String idVendedor, List<String> fotosPaths, 
            double precioMinimo, Date fechaVencimiento, Date fechaCierre) {
		super(idArticulo, titulo, descripcion, idVendedor, fotosPaths, TipoPublicacion.SUBASTA);
		this.precioMinimo = precioMinimo;
		this.fechaCierre = fechaCierre;
	}

	/*
	 * Verifica si la subasta ha vencido
	 */
	public boolean estaVencida() {
		Date ahora = new Date();
		return ahora.after(fechaCierre);
	}
	
	//Getters y setters
	
	public double getPrecioMinimo() {
		return precioMinimo;
	}

	public void setPrecioMinimo(double precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
}
