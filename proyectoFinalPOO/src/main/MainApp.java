package main;

import controller.AuthController;
import model.UserService;
import persistence.UserRepository;
import view.LoginWindow;

import javax.swing.SwingUtilities;

public class MainApp {

    public static void main(String[] args) {
        // 1. Inicializar la Capa de Persistencia (DAO)
        UserRepository userRepository = new UserRepository();

        // 2. Inicializar la Capa de Servicio (Lógica de Negocio), inyectando el Repositorio
        UserService userService = new UserService(userRepository);

        // 3. Inicializar el Controlador, inyectando el Servicio
        AuthController authController = new AuthController(userService);

        // 4. Iniciar la aplicación en el hilo de despacho de eventos de Swing (obligatorio para Swing)
        SwingUtilities.invokeLater(() -> {
            new LoginWindow(authController);
        });

        // NOTA DE PRUEBA: Puedes verificar el registro aquí
        // boolean registro = authController.manejarRegistro("30000000", "nuevoUser", "secure123", "test@mail.com", "Luis", "Ruiz", "Cali");
        // System.out.println("Intento de registro: " + registro);
    }
}