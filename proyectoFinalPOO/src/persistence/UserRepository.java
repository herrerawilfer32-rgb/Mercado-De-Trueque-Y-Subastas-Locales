package persistence;

import model.User;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class UserRepository {
    // La "Base de Datos" simulada: Key: nombreUsuario (String), Value: Objeto User
    private static Map<String, User> baseDeDatos = new HashMap<>();
    private static final String RUTA_ARCHIVO = "users.dat";

    // Bloque estático con datos de prueba
    static {
        try {
            // Intentamos cargar los datos del archivo
            baseDeDatos = (Map<String, User>) Persistencia.cargarObjeto(RUTA_ARCHIVO);
        } catch (Exception e) {
            // Si falla (ej. primera vez), inicializamos y cargamos datos dummy
            baseDeDatos = new HashMap<>();

            // Key: "admin" / ID (Cédula): "10000000"
            baseDeDatos.put("admin", new User(
                    "admin", "Juan", "Pérez", "admin@marketplace.com",
                    "12345", "10000000", "Bogotá"));

            // Key: "vendedor" / ID (Cédula): "20000000"
            baseDeDatos.put("vendedor", new User(
                    "vendedor", "María", "Gómez", "vendedor@email.com",
                    "pass", "20000000", "Medellín"));

            // Guardamos los datos iniciales
            try {
                Persistencia.guardarObjeto(RUTA_ARCHIVO, baseDeDatos);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Guarda o actualiza un objeto Usuario, usando el nombreUsuario como clave.
     */
    public void guardar(User usuario) {
        baseDeDatos.put(usuario.getNombreUsuario(), usuario);
        try {
            Persistencia.guardarObjeto(RUTA_ARCHIVO, baseDeDatos);
        } catch (IOException e) {
            e.printStackTrace();
        }
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