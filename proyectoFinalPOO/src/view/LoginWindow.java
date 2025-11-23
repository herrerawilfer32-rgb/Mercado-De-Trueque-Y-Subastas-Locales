package view;

import controller.AuthController;
import model.User;
import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    private final AuthController authController;
    private final MainWindow mainWindow; // Referencia a la ventana principal

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Constructor modificado: recibe MainWindow
    public LoginWindow(AuthController authController, MainWindow mainWindow) {
        this.authController = authController;
        this.mainWindow = mainWindow;

        setTitle("Acceso de Usuario");
        setSize(400, 250);
        // DISPOSE_ON_CLOSE: Solo cierra esta ventanita, no toda la app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(mainWindow); // Aparece centrada sobre la principal

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Panel Central (Formulario)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(new JLabel("Usuario:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Contraseña:"));
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Panel Inferior (Botones)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Entrar");
        JButton btnRegister = new JButton("Registrarse"); // Nuevo botón
        JButton btnCancel = new JButton("Cancelar");

        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> openRegisterWindow()); // Acción de registro
        btnCancel.addActionListener(e -> dispose()); // Cierra la ventana sin hacer nada

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        User usuario = authController.manejarLogin(user, pass);

        if (usuario != null) {
            // 1. AVISAR A LA VENTANA PRINCIPAL
            mainWindow.setUsuarioLogueado(usuario);

            JOptionPane.showMessageDialog(this, "¡Bienvenido de nuevo!");

            // 2. CERRAR ESTA VENTANA DE LOGIN
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Datos incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterWindow() {
        new RegisterWindow(authController, this);
    }
}