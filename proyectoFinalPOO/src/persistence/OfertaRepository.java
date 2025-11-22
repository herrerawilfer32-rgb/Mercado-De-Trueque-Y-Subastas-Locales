package persistence;

import model.Oferta;
import util.EstadoOferta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OfertaRepository {
	
	//1. Base de datos principal (busqueda por ID de oferta)
	private static final Map<String, Oferta> baseDeDatos = new HashMap<>();
	
	//2. Indice secundario (Busqueda por publicacion)
	//HashMap<String, List<Oferta>
	private static final Map<String, List<Oferta>> indicePorPublicacion = new HashMap<>();
	
	/**
	 * Guarda o actualiza un objeto Oferta.
	 */
	public void guardar(Oferta oferta) {
		//Guardar en la estructura principal
		baseDeDatos.put(oferta.getIdOferta(), oferta);
		
		//Actualizar el indice secundario
		String idPub = oferta.getIdPublicacion();
		
		//Si no existe la lista para esa publicacion, la creamos
		indicePorPublicacion.putIfAbsent(idPub, new ArrayList<>());
		
		//Obtenemos la lista y agregamos la oferta
		List<Oferta> ofertasDeLaPublicacion = indicePorPublicacion.get(idPub);
		
		//Opcional para evitar duplicados
		if (!ofertasDeLaPublicacion.contains(oferta)) {
			ofertasDeLaPublicacion.add(oferta);
		}
	}
	
	/**
	 * Busca una oferta por su ID de Oferta.
	 */
	public Oferta buscarPorIdOferta(String idOferta) {
		return baseDeDatos.get(idOferta);
	}
	
	/**
	 * Busca todas las ofertas realizadas por un ofertante específico.
	 */
	public List<Oferta> buscarOfertasPorOfertante(String idOfertante) {
		return baseDeDatos.values().stream()
				.filter(oferta -> oferta.getIdOfertante().equals(idOfertante))
				.collect(Collectors.toList());
	}
	
	//Buscar todas las ofertas
	public List<Oferta> buscarTodasLasOfertas() {
		return new ArrayList<>(baseDeDatos.values());
	}
	
	//Buscar ofertas por idPublicacion
	public List<Oferta> buscarPorPublicacion(String idPublicacion){
		return indicePorPublicacion.getOrDefault(idPublicacion, new ArrayList<>());
	}
}
