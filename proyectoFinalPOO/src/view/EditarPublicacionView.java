package view;

import controller.PublicacionController;
import model.Publicacion;
import model.User;

import javax.swing.*;
import java.awt.*;

public class EditarPublicacionView extends JFrame {

    private final PublicacionController pubController;
    private final User usuario;
    private final MainWindow mainWindow;
    private final Publicacion publicacion;

    private JTextField txtTitulo;
    private JTextArea txtDescripcion;

    public EditarPublicacionView(PublicacionController pubController, User usuario, MainWindow mainWindow,
            Publicacion publicacion) {
        this.pubController = pubController;
        this.usuario = usuario;
        this.mainWindow = mainWindow;
        this.publicacion = publicacion;

        setTitle("Editar Publicación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(mainWindow);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Título:"));
        txtTitulo = new JTextField(publicacion.getTitulo());
        formPanel.add(txtTitulo);

        formPanel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(publicacion.getDescripcion());
        txtDescripcion.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDescripcion));

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
        String nuevoTitulo = txtTitulo.getText();
        String nuevaDesc = txtDescripcion.getText();

        if (nuevoTitulo.isEmpty() || nuevaDesc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos no pueden estar vacíos.");
            return;
        }

        // Actualizamos el objeto localmente
        publicacion.setTitulo(nuevoTitulo);
        publicacion.setDescripcion(nuevaDesc);

        // Enviamos al controlador para persistir
        boolean exito = pubController.actualizarPublicacion(publicacion, usuario.getId());

        if (exito) {
            JOptionPane.showMessageDialog(this, "Publicación actualizada.");
            mainWindow.cargarPublicaciones(); // Refrescar lista principal
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
