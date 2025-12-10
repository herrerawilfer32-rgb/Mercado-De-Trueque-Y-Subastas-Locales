/*
 * Clase: RegisterWindow
 * Autores: Anggel Leal, Wilfer Herrera, David Santos
 * DescripciÃ³n: Vista de la interfaz.
 */

package view;

import controller.AuthController;
import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {

    private final AuthController authController;

    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;
    private JTextField txtCorreo;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCiudad;

    public RegisterWindow(AuthController authController, LoginWindow loginWindow) {
        this.authController = authController;

        setTitle("Registro de Usuario");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(loginWindow);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Encabezado
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setBackground(util.UIConstants.MORADO_PRINCIPAL);
        panelEncabezado.setBorder(util.UIConstants.BORDE_VACIO_20);

        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setFont(util.UIConstants.FUENTE_TITULO);
        lblTitulo.setForeground(util.UIConstants.DORADO);
        panelEncabezado.add(lblTitulo);

        add(panelEncabezado, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 10, 10));
        panelFormulario.setBackground(util.UIConstants.BLANCO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblCedula = new JLabel("Cédula (ID):*");
        lblCedula.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblCedula);
        txtId = new JTextField();
        panelFormulario.add(txtId);

        JLabel lblUsuario = new JLabel("Usuario:*");
        lblUsuario.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblUsuario);
        txtUsername = new JTextField();
        panelFormulario.add(txtUsername);

        JLabel lblContraseña = new JLabel("Contraseña:*");
        lblContraseña.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblContraseña);
        txtPassword = new JPasswordField();
        panelFormulario.add(txtPassword);

        JLabel lblConfirmar = new JLabel("Confirmar Contraseña:*");
        lblConfirmar.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblConfirmar);
        txtPasswordConfirm = new JPasswordField();
        panelFormulario.add(txtPasswordConfirm);

        JLabel lblCorreo = new JLabel("Correo:*");
        lblCorreo.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblCorreo);
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        JLabel lblNombre = new JLabel("Nombre:*");
        lblNombre.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblNombre);
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:*");
        lblApellido.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblApellido);
        txtApellido = new JTextField();
        panelFormulario.add(txtApellido);

        JLabel lblCiudad = new JLabel("Ciudad:*");
        lblCiudad.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblCiudad);
        txtCiudad = new JTextField();
        panelFormulario.add(txtCiudad);

        add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(util.UIConstants.BLANCO);

        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(util.UIConstants.VERDE_EXITO);
        btnRegistrar.setForeground(util.UIConstants.BLANCO);
        btnRegistrar.setFont(util.UIConstants.FUENTE_BOTON);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(util.UIConstants.GRIS_NEUTRAL);
        btnCancelar.setForeground(util.UIConstants.NEGRO);
        btnCancelar.setFont(util.UIConstants.FUENTE_BOTON);

        btnRegistrar.addActionListener(e -> handleRegister());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void handleRegister() {
        String id = txtId.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String passwordConfirm = new String(txtPasswordConfirm.getPassword()).trim();
        String correo = txtCorreo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String ciudad = txtCiudad.getText().trim();

        // Validar campos
        String errorMessage = validarCamposRegistro(id, username, password, passwordConfirm,
                correo, nombre, apellido, ciudad);
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(this, errorMessage, "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            authController.manejarRegistro(id, username, password, correo, nombre, apellido, ciudad);
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

    /**
     * Valida todos los campos del formulario de registro.
     * 
     * @return mensaje de error si hay problemas, null si todo está bien
     */
    private String validarCamposRegistro(String id, String username, String password,
            String passwordConfirm, String correo,
            String nombre, String apellido, String ciudad) {
        // Validar cédula
        if (!util.ValidationUtils.isNotEmpty(id)) {
            return "La cédula es obligatoria";
        }
        if (!util.ValidationUtils.isValidCedula(id)) {
            return "La cédula debe contener solo números y tener entre 6 y 15 dígitos";
        }

        // Validar usuario
        if (!util.ValidationUtils.isNotEmpty(username)) {
            return "El usuario es obligatorio";
        }
        if (!util.ValidationUtils.isLengthInRange(username, 3, 20)) {
            return "El usuario debe tener entre 3 y 20 caracteres";
        }
        if (!util.ValidationUtils.isAlphanumeric(username)) {
            return "El usuario solo puede contener letras, números y guión bajo";
        }

        // Validar contraseña
        if (!util.ValidationUtils.isNotEmpty(password)) {
            return "La contraseña es obligatoria";
        }
        if (!util.ValidationUtils.isValidPassword(password)) {
            return "La contraseña debe tener al menos 6 caracteres, incluyendo letras y números";
        }

        // Validar confirmación de contraseña
        if (!password.equals(passwordConfirm)) {
            return "Las contraseñas no coinciden";
        }

        // Validar correo
        if (!util.ValidationUtils.isNotEmpty(correo)) {
            return "El correo es obligatorio";
        }
        if (!util.ValidationUtils.isValidEmail(correo)) {
            return "El correo debe tener un formato válido (ejemplo@dominio.com)";
        }

        // Validar nombre
        if (!util.ValidationUtils.isNotEmpty(nombre)) {
            return "El nombre es obligatorio";
        }
        if (!util.ValidationUtils.hasMinLength(nombre, 2)) {
            return "El nombre debe tener al menos 2 caracteres";
        }
        if (!util.ValidationUtils.isAlphabetic(nombre)) {
            return "El nombre solo puede contener letras y espacios";
        }

        // Validar apellido
        if (!util.ValidationUtils.isNotEmpty(apellido)) {
            return "El apellido es obligatorio";
        }
        if (!util.ValidationUtils.hasMinLength(apellido, 2)) {
            return "El apellido debe tener al menos 2 caracteres";
        }
        if (!util.ValidationUtils.isAlphabetic(apellido)) {
            return "El apellido solo puede contener letras y espacios";
        }

        // Validar ciudad
        if (!util.ValidationUtils.isNotEmpty(ciudad)) {
            return "La ciudad es obligatoria";
        }
        if (!util.ValidationUtils.hasMinLength(ciudad, 3)) {
            return "La ciudad debe tener al menos 3 caracteres";
        }

        return null; // Todo válido
    }
}
