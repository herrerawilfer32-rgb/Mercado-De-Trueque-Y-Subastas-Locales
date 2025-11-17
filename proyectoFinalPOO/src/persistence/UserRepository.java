package persistence;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    // La "Base de Datos" simulada: Key: nombreUsuario (String), Value: Objeto User
    private static final Map<String, User> baseDeDatos = new HashMap<>();

    // Bloque estático con datos de prueba
    static {
        // La firma del constructor es: 
        // public User(String nombreUsuario, String nombre, String apellido, String correo, String contraseñaHash, String id, String ubicacion)
        
        // Key: "admin" / ID (Cédula): "10000000"
        baseDeDatos.put("admin", new User(
            "admin", "Juan", "Pérez", "admin@marketplace.com", 
            "12345", "10000000", "Bogotá" 
        ));
        
        // Key: "vendedor" / ID (Cédula): "20000000"
        baseDeDatos.put("vendedor", new User(
            "vendedor", "María", "Gómez", "vendedor@email.com", 
            "pass", "20000000", "Medellín"
        ));
    }

    /**
     * Guarda o actualiza un objeto Usuario, usando el nombreUsuario como clave.
     */
    public void guardar(User usuario) {
        baseDeDatos.put(usuario.getNombreUsuario(), usuario);
    }

    /**
     * Busca un usuario usando su NOMBRE DE USUARIO (búsqueda rápida O(1)).
     */
    public User buscarPorNombreUsuario(String username) {
        return baseDeDatos.get(username);
    }
    
    /**
     * Busca un usuario usando su ID (Cédula). Requiere iterar (O(n)).
     */
    public User buscarPorId(String id) {
        for (User user : baseDeDatos.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}