package model;

import persistence.PublicacionRepository;
import util.EstadoPublicacion;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PublicacionService {
	
	private final PublicacionRepository publicacionRepository;
	private final UserService userService;
	
	public PublicacionService(PublicacionRepository publicacionRepository, UserService userService) {
		this.publicacionRepository = publicacionRepository;
		this.userService = userService;
	}
	
	/*
	 * Usando el UserService podemos obtener la ubicacion del vendedor
	 */
	
	public String obtenerUbicacionVendedor(Publicacion publicacion) {
		//1. Obtenemos el id del vendedor
		String idVendedor = publicacion.getIdVendedor();
		
		//2. Usamos el userService para obtener el objeto User
		User vendedor = userService.buscarUsuarioPorId(idVendedor);
		
		// 3. Devolver la ubicación
        if (vendedor != null) {
            return vendedor.getUbicacion();
        }
        return "Ubicación no disponible";
	}

	//Buscar publicaciones por idVendedor
	public Publicacion buscarPublicacionPorId(String idPublicacion) {
		return publicacionRepository.buscarPorIdArticulo(idPublicacion);
	}
	
	/*
	 * Cierra la publicación cambiando su estado a CERRADA
	 */
	public void cerrarPublicacion(String idPublicacion) {
		// 1. Buscar la publicación por su ID
		Publicacion publicacion = publicacionRepository.buscarPorIdArticulo(idPublicacion);
		
		// 2. Validamos que exista
	    if (publicacion != null) {
	        // 3. Cambiamos el estado
	        publicacion.setEstado(util.EstadoPublicacion.CERRADA);
	        
	        // 4. Guardamos el cambio (al ser un HashMap, el put sobrescribe y actualiza)
	        publicacionRepository.guardar(publicacion);
	        
	        System.out.println("La publicación '" + publicacion.getTitulo() + "' ha sido CERRADA.");
	    } else {
	        System.out.println("Error: No se pudo cerrar la publicación. ID no encontrado.");
	    }
	}
	
	/**
     * Devuelve todas las publicaciones activas.
     */
    public List<Publicacion> buscarPublicacionesActivas() {
        return publicacionRepository.buscarPublicacionesActivas();
    }

    /**
     * Guarda una publicación nueva (Subasta o Trueque).
     */
    public void guardarPublicacion(Publicacion publicacion) {
        publicacionRepository.guardar(publicacion);
    }
}
