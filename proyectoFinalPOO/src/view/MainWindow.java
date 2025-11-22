package view;

import controller.AuthController;
import controller.PublicacionController;
import model.Publicacion;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private User usuarioLogueado = null; // INICIO: MODO INVITADO
    
    private final AuthController authController;
    private final PublicacionController pubController;

    // Componentes UI
    private JLabel lblBienvenida;
    private JButton btnLoginLogout;
    private DefaultListModel<String> listModel; // Modelo para la lista visual

    public MainWindow(AuthController authController, PublicacionController pubController) {
        this.authController = authController;
        this.pubController = pubController;

        setTitle("Mercado Local - Inicio");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
        cargarPublicaciones(); // MOSTRAR PUBLICACIONES APENAS INICIA
    }

    private void initUI() {
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblBienvenida = new JLabel("Bienvenido, Invitado");
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnLoginLogout = new JButton("Iniciar Sesión");
        btnLoginLogout.addActionListener(e -> manejarSesion());

        header.add(lblBienvenida, BorderLayout.WEST);
        header.add(btnLoginLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- CENTRO: LISTA DE PUBLICACIONES ---
        listModel = new DefaultListModel<>();
        JList<String> listaVisual = new JList<>(listModel);
        listaVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder(" Últimas Publicaciones "));
        panelCentro.add(new JScrollPane(listaVisual), BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // --- FOOTER: BOTONES DE ACCIÓN ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnVender = new JButton("💰 Publicar Artículo");
        JButton btnMisOfertas = new JButton("🤝 Ver Mis Ofertas");
        JButton btnRefrescar = new JButton("🔄 Actualizar Lista");

        // LOGICA DEL "PORTERO" (GATEKEEPER)
        btnVender.addActionListener(e -> {
            if (esInvitado()) abrirLogin(); 
            else abrirFormularioVenta();
        });

        btnMisOfertas.addActionListener(e -> {
            if (esInvitado()) abrirLogin();
            else JOptionPane.showMessageDialog(this, "Aquí verías tus ofertas (TODO)");
        });

        btnRefrescar.addActionListener(e -> cargarPublicaciones());

        footer.add(btnVender);
        footer.add(btnMisOfertas);
        footer.add(btnRefrescar);
        add(footer, BorderLayout.SOUTH);
    }

    // --- MÉTODOS LÓGICOS ---

    public void cargarPublicaciones() {
        listModel.clear();
        List<Publicacion> lista = pubController.obtenerPublicacionesActivas();
        
        if (lista.isEmpty()) {
            listModel.addElement("No hay publicaciones activas aún.");
        } else {
            for (Publicacion p : lista) {
                // Formato simple para mostrar en la lista
                String linea = String.format("[%s] %s - %s", 
                        p.getTipoPublicacion(), p.getTitulo(), p.getDescripcion());
                listModel.addElement(linea);
            }
        }
    }

    private boolean esInvitado() {
        return usuarioLogueado == null;
    }

    private void abrirLogin() {
        JOptionPane.showMessageDialog(this, "Debes iniciar sesión para realizar esta acción.");
        new LoginWindow(authController, this); // Pasamos 'this' para que el login nos actualice
    }

    private void abrirFormularioVenta() {
        new CrearPublicacionView(pubController, usuarioLogueado, this).setVisible(true);
    }

    private void manejarSesion() {
        if (esInvitado()) {
            new LoginWindow(authController, this);
        } else {
            int opt = JOptionPane.showConfirmDialog(this, "¿Cerrar Sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                setUsuarioLogueado(null); // Volver a modo invitado
            }
        }
    }

    // Llamado por LoginWindow cuando el login es exitoso
    public void setUsuarioLogueado(User user) {
        this.usuarioLogueado = user;
        if (user != null) {
            lblBienvenida.setText("Hola, " + user.getNombre());
            btnLoginLogout.setText("Cerrar Sesión");
        } else {
            lblBienvenida.setText("Bienvenido, Invitado");
            btnLoginLogout.setText("Iniciar Sesión");
        }
    }
}