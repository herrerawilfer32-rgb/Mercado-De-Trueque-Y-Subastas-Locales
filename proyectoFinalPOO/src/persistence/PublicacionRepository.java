package persistence;

import model.Publicacion;
import util.EstadoPublicacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PublicacionRepository {
	
	//Simulación de la "Base de Datos": Key: idArticulo (String), Value: Objeto Publicacion
	private static final Map<String, Publicacion> baseDeDatos = new HashMap<>();
	
	/**
	 * Guarda o actualiza un objeto Publicacion, usando el idArticulo como clave.
	 */
	public void guardar(Publicacion publicacion) {
		baseDeDatos.put(publicacion.getIdArticulo(), publicacion);
	}
	
	/**
	 * Busca una publicación por su ID de Artículo.
	 */
	public Publicacion buscarPorIdArticulo(String idArticulo) {
		return baseDeDatos.get(idArticulo);
	}
	
	/**
	 * Busca todas las publicaciones con un estado activo.
	 */
	public List<Publicacion> buscarPublicacionesActivas() {
		return baseDeDatos.values().stream()
				.filter(pub -> pub.getEstado() == EstadoPublicacion.ACTIVA)
				.collect(Collectors.toList());
		}
	
	//Buscar todas las publicaciones
	public List<Publicacion> buscarTodasLasPublicaciones() {
		return new ArrayList<>(baseDeDatos.values());
	}
}
