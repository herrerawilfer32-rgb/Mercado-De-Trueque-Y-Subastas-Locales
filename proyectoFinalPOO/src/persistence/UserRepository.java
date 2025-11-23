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
            @SuppressWarnings("unchecked")
            Map<String, User> loaded = (Map<String, User>) Persistencia.cargarObjeto(RUTA_ARCHIVO);
            baseDeDatos = loaded;
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

    public UserRepository() {
        // Constructor vacío, la carga se hace en el bloque estático o aquí si
        // preferimos
        // Pero para mantener consistencia con el código anterior, lo dejamos así o lo
        // movemos al constructor.
        // El código original lo tenía en el constructor, pero el bloque estático es
        // mejor para "base de datos estática".
        // Sin embargo, para evitar problemas de recarga, vamos a asegurarnos de que se
        // cargue.
        if (baseDeDatos.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, User> loaded = (Map<String, User>) Persistencia.cargarObjeto(RUTA_ARCHIVO);
                if (loaded != null)
                    baseDeDatos = loaded;
            } catch (Exception e) {
                // Ignorar si falla carga inicial
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