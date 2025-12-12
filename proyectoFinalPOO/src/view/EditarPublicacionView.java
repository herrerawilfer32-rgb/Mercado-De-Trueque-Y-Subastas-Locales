/**
 * Clase: EditarPublicacionView
 * Vista de la interfaz.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.2
 */

package view;

import controller.PublicacionController;
import model.Publicacion;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana para editar una publicación existente.
 * Muestra un formulario con el título y descripción, permite modificarlos y envía los cambios al controlador para actualizar en la base de datos.
 */
public class EditarPublicacionView extends JFrame {

    private final PublicacionController pubController;
    private final User usuario;
    private final MainWindow mainWindow;
    private final Publicacion publicacion;

    private JTextField txtTitulo;
    private JTextArea txtDescripcion;

    /**
     * Constructor de la ventana de edición.
     *
     * @param pubController Controlador de publicaciones
     * @param usuario       Usuario que edita
     * @param mainWindow    Ventana principal para refrescar al terminar
     * @param publicacion   Publicación a modificar
     */
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
        getContentPane().setLayout(new BorderLayout());

        initComponents();
    }

    
    private void initComponents() {
        // Encabezado
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setBackground(util.UIConstants.MORADO_PRINCIPAL);
        panelEncabezado.setBorder(util.UIConstants.BORDE_VACIO_20);

        JLabel lblTitulo = new JLabel("Editar Publicación");
        lblTitulo.setFont(util.UIConstants.FUENTE_TITULO);
        lblTitulo.setForeground(util.UIConstants.DORADO);
        panelEncabezado.add(lblTitulo);

        getContentPane().add(panelEncabezado, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFormulario.setBackground(util.UIConstants.BLANCO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTit = new JLabel("Título:");
        lblTit.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblTit);
        txtTitulo = new JTextField(publicacion.getTitulo());
        panelFormulario.add(txtTitulo);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(util.UIConstants.FUENTE_NORMAL);
        panelFormulario.add(lblDesc);
        txtDescripcion = new JTextArea(publicacion.getDescripcion());
        txtDescripcion.setLineWrap(true);
        panelFormulario.add(new JScrollPane(txtDescripcion));

        getContentPane().add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(util.UIConstants.BLANCO);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(util.UIConstants.VERDE_EXITO);
        btnGuardar.setForeground(util.UIConstants.BLANCO);
        btnGuardar.setFont(util.UIConstants.FUENTE_BOTON);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(util.UIConstants.GRIS_NEUTRAL);
        btnCancelar.setForeground(util.UIConstants.NEGRO);
        btnCancelar.setFont(util.UIConstants.FUENTE_BOTON);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
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
