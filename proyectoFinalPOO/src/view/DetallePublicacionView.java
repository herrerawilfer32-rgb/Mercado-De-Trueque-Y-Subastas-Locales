package view;

import controller.PublicacionController;
import model.Publicacion;
import model.PublicacionSubasta;
import model.PublicacionTrueque;
import model.User;
import util.TipoPublicacion;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class DetallePublicacionView extends JFrame {

    private final PublicacionController controller;
    private final Publicacion publicacion;
    private final User usuarioActual;

    public DetallePublicacionView(PublicacionController controller, Publicacion publicacion, User usuarioActual) {
        this.controller = controller;
        this.publicacion = publicacion;
        this.usuarioActual = usuarioActual;

        setTitle("Detalle de Publicación: " + publicacion.getTitulo());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel(publicacion.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(lblTitulo);

        // Descripción
        JTextArea txtDesc = new JTextArea(publicacion.getDescripcion());
        txtDesc.setEditable(false);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(new JLabel("Descripción:"));
        panelInfo.add(txtDesc);

        // Tipo y Detalles Específicos
        panelInfo.add(Box.createVerticalStrut(10));
        JLabel lblTipo = new JLabel("Tipo: " + publicacion.getTipoPublicacion());
        lblTipo.setFont(new Font("Arial", Font.ITALIC, 14));
        panelInfo.add(lblTipo);

        if (publicacion.getTipoPublicacion() == TipoPublicacion.SUBASTA) {
            PublicacionSubasta subasta = (PublicacionSubasta) publicacion;
            panelInfo.add(new JLabel("Precio Mínimo: $" + subasta.getPrecioMinimo()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            panelInfo.add(new JLabel("Cierra el: " + sdf.format(subasta.getFechaCierre())));
        } else {
            PublicacionTrueque trueque = (PublicacionTrueque) publicacion;
            panelInfo.add(new JLabel("Busca a cambio: " + trueque.getObjetosDeseados()));
        }

        // Imágenes (Lista de rutas por ahora)
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(new JLabel("Imágenes adjuntas: " + publicacion.getFotosPaths().size()));
        for (String path : publicacion.getFotosPaths()) {
            JLabel lblPath = new JLabel(path);
            lblPath.setForeground(Color.GRAY);
            panelInfo.add(lblPath);
        }

        add(new JScrollPane(panelInfo), BorderLayout.CENTER);

        // Botón Ofertar
        // Solo si no es el dueño
        if (!publicacion.getIdVendedor().equals(usuarioActual.getId())) {
            JButton btnOfertar = new JButton("Realizar Oferta");
            btnOfertar.setFont(new Font("Arial", Font.BOLD, 16));
            btnOfertar.setBackground(new Color(52, 152, 219));
            btnOfertar.setForeground(Color.WHITE);
            btnOfertar.addActionListener(e -> mostrarDialogoOferta());
            add(btnOfertar, BorderLayout.SOUTH);
        } else {
            JLabel lblDueño = new JLabel("Eres el propietario de esta publicación", SwingConstants.CENTER);
            lblDueño.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(lblDueño, BorderLayout.SOUTH);
        }
    }

    private void mostrarDialogoOferta() {
        if (publicacion.getTipoPublicacion() == TipoPublicacion.SUBASTA) {
            String input = JOptionPane.showInputDialog(this, "Ingresa el monto de tu puja ($):");
            if (input != null && !input.isEmpty()) {
                try {
                    double monto = Double.parseDouble(input);
                    boolean exito = controller.ofertar(publicacion.getIdArticulo(), usuarioActual.getId(), monto, null);
                    if (exito)
                        JOptionPane.showMessageDialog(this, "¡Puja realizada con éxito!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Monto inválido.");
                }
            }
        } else {
            String propuesta = JOptionPane.showInputDialog(this, "Describe tu oferta de trueque:");
            if (propuesta != null && !propuesta.isEmpty()) {
                boolean exito = controller.ofertar(publicacion.getIdArticulo(), usuarioActual.getId(), 0, propuesta);
                if (exito)
                    JOptionPane.showMessageDialog(this, "¡Propuesta enviada con éxito!");
            }
        }
    }
}
