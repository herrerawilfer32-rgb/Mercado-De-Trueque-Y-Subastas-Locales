package view;

import controller.AuthController;
import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {

    private final AuthController authController;
    // private final LoginWindow loginWindow; // Removed unused field

    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtCorreo;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUbicacion;

    public RegisterWindow(AuthController authController, LoginWindow loginWindow) {
        this.authController = authController;
        // this.loginWindow = loginWindow;

        setTitle("Registro de Usuario");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(loginWindow);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Cédula (ID):"));
        txtId = new JTextField();
        formPanel.add(txtId);

        formPanel.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        formPanel.add(txtCorreo);

        formPanel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        formPanel.add(txtApellido);

        formPanel.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        formPanel.add(txtUbicacion);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnRegister = new JButton("Registrarse");
        JButton btnCancel = new JButton("Cancelar");

        btnRegister.addActionListener(e -> handleRegister());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleRegister() {
        String id = txtId.getText();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String correo = txtCorreo.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String ubicacion = txtUbicacion.getText();

        if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete los campos obligatorios.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            authController.manejarRegistro(id, username, password, correo, nombre, apellido, ubicacion);
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ahora puedes iniciar sesión.");
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
