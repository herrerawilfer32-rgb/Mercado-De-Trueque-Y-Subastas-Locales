package model;

import java.util.Date;

import persistence.OfertaRepository;
import util.TipoPublicacion;

public class OfertaService {
	
	private final OfertaRepository ofertaRepository;
	private final UserService userService;
	private final PublicacionService publicacionService;
	
	public OfertaService(OfertaRepository ofertaRepository, UserService userService, PublicacionService publicacionService) {
		this.ofertaRepository = ofertaRepository;
		this.userService = userService;
		this.publicacionService = publicacionService;
	}
	
	public Oferta realizarNuevaOferta(String idOferta, String idPublicacion, String idOfertante, Date fechaOferta, double montoOferta, String descripcionTrueque) {
		//1. Validar que el ofertante exista
		User ofertante = userService.buscarUsuarioPorId(idOfertante);
		if (ofertante == null) {
			System.out.println("Error: El ofertante con ID " + idOfertante + " no existe.");
			return null;
		}
		
		//2. Validar que la publicación exista
		Publicacion publicacion = publicacionService.buscarPublicacionPorId(idPublicacion);
		if (publicacion == null) {
			System.out.println("Error: La publicación con ID " + idPublicacion + " no existe.");
			return null;
		}
		//3. Validar que la publicación esté activa
		if (publicacion.getEstado() != util.EstadoPublicacion.ACTIVA) {
			System.out.println("Error: La publicación con ID " + idPublicacion + " no está activa.");
			return null;
		}
		// Diferenciar entre subasta y trueque
		if (publicacion.getTipoPublicacion() == TipoPublicacion.SUBASTA) {
			
			//Tiene que haber un monto si o si
			if (montoOferta <= 0) {
				throw new RuntimeException("Error: En una subasta el monto debe ser positivo.");
			}
			
			//Validar que sea un monto superior a la mayor oferta
			//Traemos todas las ofertas de la publicacion
			List<Oferta> ofertasExistentes = ofertaRepository.
		}
		//4. Crear la nueva oferta
		Oferta nuevaOferta = new Oferta(idOferta, idPublicacion, idOfertante, new java.util.Date(), montoOferta, descripcionTrueque);
		
		//4. Guardar la oferta en el repositorio
		ofertaRepository.guardar(nuevaOferta);
		
		System.out.println("Oferta realizada exitosamente por el usuario " + ofertante.getNombre() + " en la publicación " + publicacion.getTitulo() + ".");
		return nuevaOferta;
	}
}
