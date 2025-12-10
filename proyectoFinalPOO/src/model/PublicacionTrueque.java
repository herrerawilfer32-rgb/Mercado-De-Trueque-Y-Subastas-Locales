/**
 * Clase: PublicacionTrueque
 *  Modelo de publicación tipo trueque.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.2
 */
package model;

import java.util.List;

import util.TipoPublicacion;

public class PublicacionTrueque extends Publicacion {

	private String objetosDeseados;

	 /**
     * Crea una publicación de trueque con toda su información.
     *
     * @param idArticulo   Identificador del artículo publicado.
     * @param titulo       Título de la publicación.
     * @param descripcion  Descripción del artículo ofrecido.
     * @param idVendedor   ID del usuario que publica el artículo.
     * @param fotosPaths   Lista con las rutas de las imágenes asociadas.
     * @param objetosDeseados Cadena que contiene los objetos deseados,
     *                        separados por comas.
     */
	public PublicacionTrueque(String idArticulo, String titulo, String descripcion,
			String idVendedor, List<String> fotosPaths,
			String objetosDeseados) {
		super(idArticulo, titulo, descripcion, idVendedor, fotosPaths, TipoPublicacion.TRUEQUE);
		this.objetosDeseados = objetosDeseados;
	}

	/*
	 * Método para encontrar Match entre objetos deseados y ofrecidos
	 */
	public boolean hayIntereses(String descripcionTrueque) {
		// Chequeamos si la descripcion del trueque contiene alguno de los objetos
		// deseados
		String[] deseosArray = objetosDeseados.toLowerCase().split(","); // Suponemos que los objetos deseados están
																			// separados por comas
		String descripcionLower = descripcionTrueque.toLowerCase();

		for (String deseo : deseosArray) {
			if (descripcionLower.contains(deseo.trim())) {
				return true; // Hay al menos un objeto deseado en la descripción del trueque
			}
		}
		return false; // No se encontraron coincidencias
	}

	// Getters y setters
	public String getObjetosDeseados() {
		return objetosDeseados;
	}

	public void setObjetosDeseados(String objetosDeseados) {
		this.objetosDeseados = objetosDeseados;
	}

}
