package controller;

import model.User;
import service.UserService;

public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 1. MÉTODO REQUERIDO POR LoginWindow
    public User manejarLogin(String username, String password) {
        // La lógica de inicio de sesión se centraliza aquí
        return userService.iniciarSesion(username, password);
    }

    // 2. MÉTODO REQUERIDO POR LoginWindow
    public boolean manejarRegistro(String id, String username, String password, String correo, String nombre,
            String apellido, String ubicacion) {
        // La lógica de registro se centraliza aquí

        // Asumiendo que UserService tiene un método registrarUsuario
        return userService.registrarUsuario(id, username, password, correo, nombre, apellido, ubicacion);
    }

}