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

	public Publicacion buscarPublicacionPorId(String idPublicacion) {
		return publicacionRepository.buscarPorIdArticulo(idPublicacion);
	}
}
