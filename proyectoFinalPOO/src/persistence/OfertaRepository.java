package persistence;

import model.Oferta;
import util.EstadoOferta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OfertaRepository {
	
	//Simulación de la "Base de Datos": Key: idOferta (String), Value: Objeto Oferta
	private static final Map<String, Oferta> baseDeDatos = new HashMap<>();
	
	/**
	 * Guarda o actualiza un objeto Oferta, usando el idOferta como clave.
	 */
	public void guardar(Oferta oferta) {
		baseDeDatos.put(oferta.getIdOferta(), oferta);
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
	
	//TODO: corregir método, cambiar por mala eficiencia
	public List<Oferta> buscarPorPublicacion(String idPublicacion){
		//Creamos una lista vacía para guardar los resultados
		List<Oferta> resultados = new ArrayList<>();
		
		//Recorremos todas las ofertas que existen en el sistema 
		//baseDeDatos.values() nos da la coleccion de objetos oferta
		for (Oferta oferta : baseDeDatos.values()) {
			
		}
	}
}
