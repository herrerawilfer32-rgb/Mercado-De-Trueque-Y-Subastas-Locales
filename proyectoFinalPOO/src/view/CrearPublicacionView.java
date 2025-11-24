package view;

import controller.PublicacionController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class CrearPublicacionView extends JFrame {

    private final PublicacionController controller;
    private final User vendedor;
    private final MainWindow mainWindow; // Para refrescar la lista al terminar

    private JTextField txtTitulo, txtPrecio;
    private JTextArea txtDescripcion, txtDeseos;
    private JComboBox<String> cmbTipo;
    private JPanel panelDinamico;
    private CardLayout cardLayout;

    private java.util.List<String> fotosSeleccionadas = new java.util.ArrayList<>();
    private JLabel lblFotosInfo;

    public CrearPublicacionView(PublicacionController controller, User vendedor, MainWindow mainWindow) {
        this.controller = controller;
        this.vendedor = vendedor;
        this.mainWindow = mainWindow;

        setTitle("Nueva Publicación");
        setSize(400, 600); // Aumentamos un poco el alto
        setLocationRelativeTo(mainWindow);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // --- Formulario Común ---
        JPanel panelForm = new JPanel(new GridLayout(0, 1, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Título del Artículo:"));
        txtTitulo = new JTextField();
        panelForm.add(txtTitulo);

        panelForm.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        panelForm.add(new JScrollPane(txtDescripcion));

        panelForm.add(new JLabel("Tipo de Publicación:"));
        cmbTipo = new JComboBox<>(new String[] { "SUBASTA", "TRUEQUE" });
        panelForm.add(cmbTipo);

        // --- Sección de Imágenes ---
        JPanel panelFotos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnFotos = new JButton("Seleccionar Imágenes");
        lblFotosInfo = new JLabel("0 imágenes seleccionadas");

        btnFotos.addActionListener(e -> seleccionarFotos());

        panelFotos.add(btnFotos);
        panelFotos.add(lblFotosInfo);
        panelForm.add(new JLabel("Imágenes:"));
        panelForm.add(panelFotos);

        // --- Panel Dinámico (Cambia según el combo) ---
        cardLayout = new CardLayout();
        panelDinamico = new JPanel(cardLayout);

        // Opción A: Panel Subasta
        JPanel panelSubasta = new JPanel(new GridLayout(0, 1));
        panelSubasta.add(new JLabel("Precio Mínimo ($):"));
        txtPrecio = new JTextField();
        panelSubasta.add(txtPrecio);
        panelDinamico.add(panelSubasta, "SUBASTA");

        // Opción B: Panel Trueque
        JPanel panelTrueque = new JPanel(new GridLayout(0, 1));
        panelTrueque.add(new JLabel("¿Qué buscas a cambio?"));
        txtDeseos = new JTextArea(2, 20);
        panelTrueque.add(new JScrollPane(txtDeseos));
        panelDinamico.add(panelTrueque, "TRUEQUE");

        panelForm.add(panelDinamico);

        // Listener para cambiar campos
        cmbTipo.addActionListener(e -> cardLayout.show(panelDinamico, (String) cmbTipo.getSelectedItem()));

        add(panelForm, BorderLayout.CENTER);

        // --- Botón Guardar ---
        JButton btnPublicar = new JButton("PUBLICAR AHORA");
        btnPublicar.setBackground(new Color(46, 204, 113));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setFont(new Font("Arial", Font.BOLD, 14));

        btnPublicar.addActionListener(e -> manejarPublicacion());
        add(btnPublicar, BorderLayout.SOUTH);
    }

    private void seleccionarFotos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser
                .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File[] files = fileChooser.getSelectedFiles();
            for (java.io.File file : files) {
                fotosSeleccionadas.add(file.getAbsolutePath());
            }
            lblFotosInfo.setText(fotosSeleccionadas.size() + " imágenes seleccionadas");
        }
    }

    private void manejarPublicacion() {
        String titulo = txtTitulo.getText();
        String desc = txtDescripcion.getText();
        String tipo = (String) cmbTipo.getSelectedItem();
        boolean exito = false;

        if (titulo.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y descripción son obligatorios.");
            return;
        }

        if (tipo.equals("SUBASTA")) {
            try {
               double precio = Double.parseDouble(txtPrecio.getText());
            	exito = controlador.crearSubasta(
            	        titulo,
            	        desc,
            	        vendedor,
            	        precio,
            	        7,                 // días de duración
            	        new ArrayList<>()  // por ahora, sin fotos
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número.");
                return;
            }
        } else {
            exito = controlador.crearTrueque(
        	        titulo,
        	        desc,
        	        vendedor,
        	        deseos,
        	        new ArrayList<>() // por ahora, sin fotos
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Artículo publicado con éxito!");
            mainWindow.cargarPublicaciones(); // Refrescar la lista de atrás
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar.");
        }
    }
}
