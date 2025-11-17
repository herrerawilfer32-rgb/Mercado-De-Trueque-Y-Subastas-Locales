package view;

import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ActionListener {

    private final AuthController authController;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginWindow(AuthController authController) {
        this.authController = authController;
        setTitle("Mercado Local - Acceso");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra los componentes

        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Campos de entrada
        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);
        
        // Paneles para mejor organización
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Usuario:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(passwordField);
        add(inputPanel);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Botón de Login (Acceder)
        loginButton = new JButton("Acceder");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);
        
        // Botón de Registro (Nuevo)
        JButton registerButton = new JButton("Registrar");
        // Usamos un ActionListener para llamar directamente al método handleRegistration
        registerButton.addActionListener(e -> handleRegistration()); 
        buttonPanel.add(registerButton);

        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()); 

        User loggedInUser = authController.manejarLogin(username, password);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido, " + loggedInUser.getNombre() + "!", 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            // TODO: Abrir la Ventana Principal (1.2)
            System.out.println("-> ABRIENDO VENTANA PRINCIPAL para usuario " + loggedInUser.getNombreUsuario());
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o Contraseña inválidos. (Prueba con 'admin' / '12345' )", 
                "Error de Autenticación", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegistration() {
        // Recolección de datos mediante diálogos de entrada simples
        String id = JOptionPane.showInputDialog(this, "Cédula/ID Único:");
        String username = JOptionPane.showInputDialog(this, "Nombre de Usuario:");
        String password = JOptionPane.showInputDialog(this, "Contraseña:");
        String nombre = JOptionPane.showInputDialog(this, "Nombre:");
        String apellido = JOptionPane.showInputDialog(this, "Apellido:");
        String correo = JOptionPane.showInputDialog(this, "Correo:");
        String ubicacion = JOptionPane.showInputDialog(this, "Ubicación:");

        // Validación básica de campos clave (puedes mejorar esto)
        if (id == null || username == null || password == null || id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID, Usuario y Contraseña son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Llamar al controlador para el registro
        boolean success = authController.manejarRegistro(id, username, password, correo, nombre, apellido, ubicacion);

        // Mostrar resultado
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "¡Registro Exitoso! Usuario: " + username + " guardado.", 
                "Registro", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Fallo el registro. El ID o el Nombre de Usuario ya existen.", 
                "Registro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}