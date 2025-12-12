/**
 * Nombre del programa: TrueQ
 * Clase: MainApp
 * Punto de entrada de la aplicación. Inicializa los repositorios,servicios y controladores, luego lanza la ventana principal del sistema.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.0
 */

package main;

import javax.swing.SwingUtilities;
import view.MainWindow;
import controller.AuthController;
import controller.PublicacionController;
import controller.AdminController;
import controller.ReporteController;
import service.UserService;
import service.PublicacionService;
import service.OfertaService;
import service.AdminService;
import service.ReporteService;
import persistence.UserRepository;
import persistence.PublicacionRepository;
import persistence.OfertaRepository;
import persistence.ReporteRepository;

public class MainApp {
    
    /**
     * Método principal del programa. Se ejecuta al iniciar la aplicación.
     * Se usa SwingUtilities.invokeLater para asegurar que la interfaz gráfica se construya en el hilo de eventos de Swing (Event Dispatch Thread).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Repositorios
            
            /**
             * Repositorios encargados de almacenar y cargar datos. Funcionan como la capa de persistencia.
             */
            UserRepository userRepo = new UserRepository();
            PublicacionRepository pubRepo = new PublicacionRepository();
            OfertaRepository ofertaRepo = new OfertaRepository();
            persistence.ChatRepository chatRepo = new persistence.ChatFileRepository();
            ReporteRepository reporteRepo = new ReporteRepository();

            // 2. Servicios
            
            /**
             * Servicios que contienen la lógica del negocio:
             * - Manejo de usuarios
             * - Manejo de publicaciones
             * - Manejo de ofertas
             * - Administración
             * - Reportes
             */
            UserService userService = new UserService(userRepo);
            PublicacionService pubService = new PublicacionService(pubRepo, userService, ofertaRepo);
            OfertaService ofertaService = new OfertaService(ofertaRepo, userService, pubService);
            AdminService adminService = new AdminService(userRepo, pubRepo, ofertaRepo);
            ReporteService reporteService = new ReporteService(reporteRepo, userRepo);

            // 3. Controladores

             /**
             * Los controladores conectan la vista con los servicios y permiten que la interfaz gráfica invoque acciones del sistema
             */
            controller.ChatController chatController = new controller.ChatController(chatRepo, ofertaRepo, pubRepo);
            AuthController authController = new AuthController(userService);
            PublicacionController pubController = new PublicacionController(pubService, ofertaService, chatController);
            AdminController adminController = new AdminController(adminService);
            ReporteController reporteController = new ReporteController(reporteService);

            // 4. Vista Principal
            
            /**
             * Se crea la ventana principal del sistema (GUI) enviando todos los controladores que necesita para funciona
             */
            MainWindow main = new MainWindow(authController, pubController, chatController, adminController,
                    reporteController);
            main.setVisible(true);
        });
    }
}
