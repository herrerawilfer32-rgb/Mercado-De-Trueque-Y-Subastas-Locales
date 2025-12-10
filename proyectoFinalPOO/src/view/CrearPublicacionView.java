/**
 * Clase: CrearPublicacionView
 *Vista encargada de crear nuevas publicaciones de tipo SUBASTA o TRUEQUE.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.1
 */

package view;

import controller.PublicacionController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CrearPublicacionView extends JFrame {

    private final PublicacionController controller;
    private final User vendedor;
    private final MainWindow mainWindow; // Para refrescar la lista al terminar

    private JTextField txtTitulo, txtPrecio;
    private JTextArea txtDescripcion, txtDeseos;
    private JComboBox<String> cmbTipo, cmbCategoria, cmbCondicion;
    private JPanel panelDinamico;
    private CardLayout cardLayout;
    private java.util.List<String> fotosSeleccionadas; // Lista de rutas de fotos
    private JPanel panelPreviewFotos; // Panel para mostrar previews
    private persistence.ConfiguracionRepository configRepo;

    /**
     * Construye la vista para crear una nueva publicación.
     *
     * @param controller  Controlador que gestiona la creación de publicaciones.
     * @param vendedor    Usuario que está publicando el artículo.
     * @param mainWindow  Ventana principal para refrescar la lista tras publicar.
     */
    public CrearPublicacionView(PublicacionController controller, User vendedor, MainWindow mainWindow) {
        this.controller = controller;
        this.vendedor = vendedor;
        this.mainWindow = mainWindow;

        setTitle("Nueva Publicación");
        setSize(500, 650);
        setLocationRelativeTo(mainWindow);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        fotosSeleccionadas = new ArrayList<>();
        configRepo = new persistence.ConfiguracionRepository();

        initComponents();
    }

    private void initComponents() {
        // Encabezado
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setBackground(util.UIConstants.MORADO_PRINCIPAL);
        panelEncabezado.setBorder(util.UIConstants.BORDE_VACIO_20);

        JLabel lblTitulo = new JLabel("Nueva Publicación");
        lblTitulo.setFont(util.UIConstants.FUENTE_TITULO);
        lblTitulo.setForeground(util.UIConstants.DORADO);
        panelEncabezado.add(lblTitulo);

        getContentPane().add(panelEncabezado, BorderLayout.NORTH);

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(0, 1, 5, 5));
        panelForm.setBackground(util.UIConstants.VERDE_AZULADO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTituloArt = new JLabel("Título del Artículo:");
        lblTituloArt.setForeground(util.UIConstants.BLANCO);
        lblTituloArt.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblTituloArt);
        txtTitulo = new JTextField();
        panelForm.add(txtTitulo);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setForeground(util.UIConstants.BLANCO);
        lblDesc.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblDesc);
        txtDescripcion = new JTextArea(3, 20);
        panelForm.add(new JScrollPane(txtDescripcion));

        JLabel lblTipoPub = new JLabel("Tipo de Publicación:");
        lblTipoPub.setForeground(util.UIConstants.BLANCO);
        lblTipoPub.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblTipoPub);
        cmbTipo = new JComboBox<>(new String[] { "SUBASTA", "TRUEQUE" });
        panelForm.add(cmbTipo);

        // Categoría
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setForeground(util.UIConstants.BLANCO);
        lblCategoria.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblCategoria);

        java.util.List<String> categorias = configRepo.obtenerConfiguracion().getCategorias();
        categorias.add("+ Agregar nueva categoría...");
        cmbCategoria = new JComboBox<>(categorias.toArray(new String[0]));
        cmbCategoria.addActionListener(e -> manejarSeleccionCategoria());
        panelForm.add(cmbCategoria);

        // Condición
        JLabel lblCondicion = new JLabel("Condición del Artículo:");
        lblCondicion.setForeground(util.UIConstants.BLANCO);
        lblCondicion.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblCondicion);

        cmbCondicion = new JComboBox<>(new String[] {
                "Nuevo",
                "Usado como nuevo",
                "Usado buen estado",
                "Aceptable"
        });
        panelForm.add(cmbCondicion);

        // Panel Dinámico
        cardLayout = new CardLayout();
        panelDinamico = new JPanel(cardLayout);
        panelDinamico.setBackground(util.UIConstants.VERDE_AZULADO);

        // Panel Subasta
        JPanel panelSubasta = new JPanel(new GridLayout(0, 1));
        panelSubasta.setBackground(util.UIConstants.VERDE_AZULADO);
        JLabel lblPrecio = new JLabel("Precio Mínimo ($):");
        lblPrecio.setForeground(util.UIConstants.BLANCO);
        lblPrecio.setFont(util.UIConstants.FUENTE_NORMAL);
        panelSubasta.add(lblPrecio);
        txtPrecio = new JTextField();
        panelSubasta.add(txtPrecio);
        panelDinamico.add(panelSubasta, "SUBASTA");

        // Panel Trueque
        JPanel panelTrueque = new JPanel(new GridLayout(0, 1));
        panelTrueque.setBackground(util.UIConstants.VERDE_AZULADO);
        JLabel lblDeseos = new JLabel("¿Qué buscas a cambio?");
        lblDeseos.setForeground(util.UIConstants.BLANCO);
        lblDeseos.setFont(util.UIConstants.FUENTE_NORMAL);
        panelTrueque.add(lblDeseos);
        txtDeseos = new JTextArea(2, 20);
        panelTrueque.add(new JScrollPane(txtDeseos));
        panelDinamico.add(panelTrueque, "TRUEQUE");

        panelForm.add(panelDinamico);

        // Fotos
        JLabel lblFotos = new JLabel("Fotos del Artículo:");
        lblFotos.setForeground(util.UIConstants.BLANCO);
        lblFotos.setFont(util.UIConstants.FUENTE_NORMAL);
        panelForm.add(lblFotos);

        JButton btnAgregarFoto = new JButton("📷 Seleccionar Imágenes");
        btnAgregarFoto.setBackground(util.UIConstants.MORADO_SECUNDARIO);
        btnAgregarFoto.setForeground(util.UIConstants.DORADO);
        btnAgregarFoto.setFont(util.UIConstants.FUENTE_BOTON);
        btnAgregarFoto.addActionListener(e -> seleccionarImagenes());
        panelForm.add(btnAgregarFoto);

        // Panel preview fotos
        panelPreviewFotos = new JPanel();
        panelPreviewFotos.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelPreviewFotos.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelPreviewFotos.setPreferredSize(new Dimension(380, 100));
        panelPreviewFotos.setBackground(util.UIConstants.BLANCO);
        panelForm.add(new JScrollPane(panelPreviewFotos));

        // Listener para cambiar campos
        cmbTipo.addActionListener(e -> cardLayout.show(panelDinamico, (String) cmbTipo.getSelectedItem()));

        getContentPane().add(panelForm, BorderLayout.CENTER);

        // Panel Inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(util.UIConstants.MORADO_PRINCIPAL);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(util.UIConstants.GRIS_NEUTRAL);
        btnCerrar.setForeground(util.UIConstants.NEGRO);
        btnCerrar.setFont(util.UIConstants.FUENTE_BOTON);
        btnCerrar.addActionListener(e -> dispose());

        JButton btnPublicar = new JButton("PUBLICAR AHORA");
        btnPublicar.setBackground(util.UIConstants.VERDE_EXITO);
        btnPublicar.setForeground(util.UIConstants.BLANCO);
        btnPublicar.setFont(util.UIConstants.FUENTE_BOTON);
        btnPublicar.addActionListener(e -> manejarPublicacion());

        panelInferior.add(btnCerrar);
        panelInferior.add(btnPublicar);

        getContentPane().add(panelInferior, BorderLayout.SOUTH);
    }
/**
 * Abre un selector de archivos para elegir imágenes del artículo.
 * Permite selección múltiple y almacena las rutas en la lista interna.
 * Luego actualiza las miniaturas en pantalla.
 */
    private void seleccionarImagenes() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif", "bmp"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File[] archivos = fileChooser.getSelectedFiles();

            for (java.io.File archivo : archivos) {
                fotosSeleccionadas.add(archivo.getAbsolutePath());
            }

            actualizarPreviewFotos();
        }
    }

    private void actualizarPreviewFotos() {
        panelPreviewFotos.removeAll();

        for (int i = 0; i < fotosSeleccionadas.size(); i++) {
            final int index = i;
            String ruta = fotosSeleccionadas.get(i);

            JPanel cardFoto = new JPanel(new BorderLayout());
            cardFoto.setPreferredSize(new Dimension(80, 80));
            cardFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            javax.swing.ImageIcon icon = util.ImageUtils.loadImage(ruta, 75, 75);
            JLabel lblFoto = new JLabel(icon);
            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

            JButton btnEliminar = new JButton("X");
            btnEliminar.setFont(new Font("Arial", Font.BOLD, 10));
            btnEliminar.setPreferredSize(new Dimension(20, 20));
            btnEliminar.setMargin(new Insets(0, 0, 0, 0));
            btnEliminar.addActionListener(e -> {
                fotosSeleccionadas.remove(index);
                actualizarPreviewFotos();
            });

            cardFoto.add(lblFoto, BorderLayout.CENTER);
            cardFoto.add(btnEliminar, BorderLayout.NORTH);

            panelPreviewFotos.add(cardFoto);
        }

        panelPreviewFotos.revalidate();
        panelPreviewFotos.repaint();
    }

    private void manejarSeleccionCategoria() {
        String seleccion = (String) cmbCategoria.getSelectedItem();
        if (seleccion != null && seleccion.equals("+ Agregar nueva categoría...")) {
            String nuevaCategoria = JOptionPane.showInputDialog(this,
                    "Ingrese el nombre de la nueva categoría:",
                    "Nueva Categoría",
                    JOptionPane.PLAIN_MESSAGE);

            if (nuevaCategoria != null && !nuevaCategoria.trim().isEmpty()) {
                model.ConfiguracionGlobal config = configRepo.obtenerConfiguracion();
                config.agregarCategoria(nuevaCategoria.trim());
                configRepo.actualizarConfiguracion(config);

                // Recargar el combo
                cmbCategoria.removeAllItems();
                for (String cat : config.getCategorias()) {
                    cmbCategoria.addItem(cat);
                }
                cmbCategoria.addItem("+ Agregar nueva categoría...");
                cmbCategoria.setSelectedItem(nuevaCategoria.trim());
            } else {
                cmbCategoria.setSelectedIndex(0);
            }
        }
    }

    private void manejarPublicacion() {
        String titulo = txtTitulo.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();
        String categoria = (String) cmbCategoria.getSelectedItem();
        String condicionStr = (String) cmbCondicion.getSelectedItem();

        // Validar categoría
        if (categoria == null || categoria.equals("+ Agregar nueva categoría...")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría válida", "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convertir condición string a enum
        util.CondicionArticulo condicion = convertirCondicion(condicionStr);

        String errorMessage = validarCamposComunes(titulo, desc);
        if (errorMessage != null) {
            JOptionPane.showMessageDialog(this, errorMessage, "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = false;

        if (tipo.equals("SUBASTA")) {
            String precioTexto = txtPrecio.getText().trim();

            errorMessage = validarPrecioSubasta(precioTexto);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(precioTexto);
            exito = controller.crearSubasta(
                    titulo,
                    desc,
                    vendedor,
                    precio,
                    7,
                    new ArrayList<>(fotosSeleccionadas),
                    categoria,
                    condicion);
        } else {
            String deseos = txtDeseos.getText().trim();

            errorMessage = validarDeseosTrueque(deseos);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(this, errorMessage, "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            exito = controller.crearTrueque(
                    titulo,
                    desc,
                    vendedor,
                    deseos,
                    new ArrayList<>(fotosSeleccionadas),
                    categoria,
                    condicion);
        }

        if (exito) {
            if (fotosSeleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Artículo publicado con éxito.\nNota: No agregaste fotos.",
                        "Publicación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡Artículo publicado con éxito!");
            }
            mainWindow.cargarPublicaciones();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la publicación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private util.CondicionArticulo convertirCondicion(String condicionStr) {
        switch (condicionStr) {
            case "Nuevo":
                return util.CondicionArticulo.NUEVO;
            case "Usado como nuevo":
                return util.CondicionArticulo.USADO_COMO_NUEVO;
            case "Usado buen estado":
                return util.CondicionArticulo.USADO_BUEN_ESTADO;
            case "Aceptable":
                return util.CondicionArticulo.ACEPTABLE;
            default:
                return util.CondicionArticulo.NUEVO;
        }
    }

    private String validarCamposComunes(String titulo, String desc) {
        if (!util.ValidationUtils.isNotEmpty(titulo)) {
            return "El título es obligatorio";
        }
        if (!util.ValidationUtils.isLengthInRange(titulo, 5, 100)) {
            return "El título debe tener entre 5 y 100 caracteres";
        }

        if (!util.ValidationUtils.isNotEmpty(desc)) {
            return "La descripción es obligatoria";
        }
        if (!util.ValidationUtils.isLengthInRange(desc, 10, 500)) {
            return "La descripción debe tener entre 10 y 500 caracteres";
        }

        return null;
    }

    private String validarPrecioSubasta(String precioTexto) {
        if (!util.ValidationUtils.isNotEmpty(precioTexto)) {
            return "El precio mínimo es obligatorio";
        }

        Double precio = util.ValidationUtils.tryParseDouble(precioTexto);
        if (precio == null) {
            return "El precio debe ser un número válido";
        }

        if (precio <= 0) {
            return "El precio debe ser mayor a cero";
        }

        if (precio > 1500000000) {
            return "El precio no puede exceder $1,500,000,000 COP";
        }

        return null;
    }

    private String validarDeseosTrueque(String deseos) {
        if (!util.ValidationUtils.isNotEmpty(deseos)) {
            return "Debes especificar qué buscas a cambio";
        }

        if (!util.ValidationUtils.hasMinLength(deseos, 10)) {
            return "La descripción de lo que buscas debe tener al menos 10 caracteres";
        }

        return null;
    }
}
