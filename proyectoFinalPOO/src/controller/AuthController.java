package controller;

import model.UserService;
import model.User;

public class AuthController {
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Maneja la solicitud de Login (usando USERNAME y Contraseña).
     */
    public User manejarLogin(String username, String password) {
        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("Login exitoso. Bienvenido: " + user.getNombre() + " (ID: " + user.getId() + ")");
        } else {
            System.out.println("Error de Login: Nombre de Usuario o Contraseña inválida.");
        }
        return user;
    }

    /**
     * Maneja la solicitud de Registro, validando unicidad por USERNAME y ID.
     */
    public boolean manejarRegistro(String id, String username, String password, String correo, String nombre, String apellido, String ubicacion) {
        boolean exito = userService.registrarUsuario(id, username, password, correo, nombre, apellido, ubicacion);

        if (exito) {
            System.out.println("Registro exitoso. Usuario " + username + " guardado (Cédula: " + id + ").");
        } else {
            System.out.println("Error de Registro. Revise si el ID o el Nombre de Usuario ya existen.");
        }
        return exito;
    }
}