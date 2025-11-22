package main;

import javax.swing.SwingUtilities;
import view.MainWindow;
import controller.AuthController;
import controller.PublicacionController; // Nuevo
import model.UserService;
import model.PublicacionService;
import model.OfertaService;
import persistence.UserRepository;
import persistence.PublicacionRepository;
import persistence.OfertaRepository;

public class MainApp {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Repositorios
            UserRepository userRepo = new UserRepository();
            PublicacionRepository pubRepo = new PublicacionRepository();
            OfertaRepository ofertaRepo = new OfertaRepository();
            
            // 2. Servicios
            UserService userService = new UserService(userRepo);
            PublicacionService pubService = new PublicacionService(pubRepo, userService);
            OfertaService ofertaService = new OfertaService(ofertaRepo, userService, pubService);
            
            // 3. Controladores
            AuthController authController = new AuthController(userService);
            // Nuevo controlador para las publicaciones
            PublicacionController pubController = new PublicacionController(pubService);
            
            // 4. Vista Principal
            // Le pasamos ambos controladores para que pueda manejar Login y Publicaciones
            MainWindow main = new MainWindow(authController, pubController);
            main.setVisible(true);
        });
    }
}