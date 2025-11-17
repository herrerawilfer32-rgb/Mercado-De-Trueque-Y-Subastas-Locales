package model;

import persistence.UserRepository;

public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// Método login (Búsqueda por Username)
	public User login(String username, String password) {
		
		User user = userRepository.buscarPorNombreUsuario(username);
		
		// CORRECCIÓN: Usamos .equalsIgnoreCase(password) para evitar errores de mayúsculas/minúsculas
		if(user != null && user.getContraseñaHash().equalsIgnoreCase(password)) { 
			return user; // Login exitoso
		}
		return null; // Login fallido
	}
	
	/**
     * Registra un nuevo usuario. Valida que el USERNAME y el ID (Cédula) sean únicos.
     */
	public boolean registrarUsuario(String id, String username, String password, String correo, 
									String nombre, String apellido, String ubicacion){
		
		// 1. Validar unicidad del USERNAME
		if (userRepository.buscarPorNombreUsuario(username) != null) {
			return false; 
		}
		
		// 2. Validar unicidad del ID (Cédula)
		if (userRepository.buscarPorId(id) != null) {
			return false; 
		}
		
		// 3. Crear y guardar el nuevo usuario (orden de argumentos alineado)
		User nuevoUsuario = new User(
		    username,   
		    nombre,     
		    apellido,   
		    correo,     
		    password,   
		    id,         
		    ubicacion   
		);
		
		userRepository.guardar(nuevoUsuario);
		return true;
	}
}
