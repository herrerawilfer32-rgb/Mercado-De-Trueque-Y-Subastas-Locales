package main;

import javax.swing.SwingUtilities;
import view.MainWindow;
import controller.AuthController;
import controller.PublicacionController;
import service.UserService;
import service.PublicacionService;
import service.OfertaService;
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
            PublicacionService pubService = new PublicacionService(pubRepo, userService, ofertaRepo);
            OfertaService ofertaService = new OfertaService(ofertaRepo, userService, pubService);


           // 3. Controladores
            AuthController authController = new AuthController(userService);
            // Controlador para las publicaciones (usa también ofertas)
            PublicacionController pubController = new PublicacionController(pubService, ofertaService);
            
            // Este ChatController creado aquí realmente no se usa, puedes quitarlo
            // o dejarlo si luego piensas usarlo.
            // ChatController chatController = new ChatController(chatRepository);


            // 4. Vista Principal
            // Le pasamos ambos controladores para que pueda manejar Login y Publicaciones
            MainWindow main = new MainWindow(authController, pubController);
            main.setVisible(true);
        });
    }
}
