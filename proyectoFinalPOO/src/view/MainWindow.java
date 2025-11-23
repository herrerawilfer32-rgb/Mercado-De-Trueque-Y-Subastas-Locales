package view;

import controller.AuthController;
import controller.PublicacionController;
// >>> CHAT
import controller.ChatController;
import model.chat.Chat;
import persistence.ChatRepository;
import persistence.ChatFileRepository;
// <<< CHAT

import model.Publicacion;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private User usuarioLogueado = null; // INICIO: MODO INVITADO

    private final AuthController authController;
    private final PublicacionController pubController;
    // >>> CHAT
    private final ChatController chatController;
    // <<< CHAT

    // Componentes UI
    private JLabel lblBienvenida;
    private JButton btnLoginLogout;
    private DefaultListModel<Publicacion> listModel; // Cambiado a Publicacion
    private JList<Publicacion> listaVisual;

    // >>> CHAT - componentes del módulo de chat
    private JTabbedPane pestañasCentro;
    private PanelListaChats panelListaChats;
    private PanelChatDetalle panelChatDetalle;
    // <<< CHAT

    public MainWindow(AuthController authController, PublicacionController pubController) {
        this.authController = authController;
        this.pubController = pubController;

        // >>> CHAT - inicializar controlador de chat
        ChatRepository chatRepository = new ChatFileRepository();
        this.chatController = new ChatController(chatRepository);
        // <<< CHAT

        setTitle("Mercado Local - Inicio");
        setSize(900, 600);
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
        listaVisual = new JList<>(listModel);
        listaVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Renderizador personalizado para mostrar texto bonito
        listaVisual.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Publicacion) {
                    Publicacion p = (Publicacion) value;
                    setText(String.format("[%s] %s - %s ($%.2f)",
                            p.getTipoPublicacion(), p.getTitulo(), p.getDescripcion(),
                            (p instanceof model.PublicacionSubasta) ? ((model.PublicacionSubasta) p).getPrecioMinimo()
                                    : 0.0));
                }
                return this;
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder(" Últimas Publicaciones "));
        panelCentro.add(new JScrollPane(listaVisual), BorderLayout.CENTER);

        // >>> CHAT - envolver centro en pestañas y agregar pestaña de chats
        pestañasCentro = new JTabbedPane();
        pestañasCentro.addTab("Publicaciones", panelCentro);

        // Paneles de chat
        panelListaChats = new PanelListaChats(chatController, new PanelListaChats.ChatSeleccionListener() {
            @Override
            public void abrirChat(Chat chatSeleccionado) {
                panelChatDetalle.setChatActual(chatSeleccionado);
                pestañasCentro.setSelectedIndex(1); // Cambiar a tab de Chats
            }
        });

        panelChatDetalle = new PanelChatDetalle(chatController);

        JSplitPane splitChats = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                panelListaChats,
                panelChatDetalle
        );
        splitChats.setDividerLocation(300);

        pestañasCentro.addTab("Chats", splitChats);

        // Agregar pestañas al centro de la ventana
        add(pestañasCentro, BorderLayout.CENTER);
        // <<< CHAT

        // --- FOOTER: BOTONES DE ACCIÓN ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnVender = new JButton("💰 Publicar Artículo");
        JButton btnMisOfertas = new JButton("🤝 Ver Mis Ofertas");
        JButton btnRefrescar = new JButton("🔄 Actualizar Lista");

        // Nuevos botones CRUD
        JButton btnVerDetalle = new JButton("👁️ Ver Detalle");
        JButton btnEditar = new JButton("✏️ Editar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");

        // LOGICA DEL "PORTERO" (GATEKEEPER)
        btnVender.addActionListener(e -> {
            if (esInvitado())
                abrirLogin();
            else
                abrirFormularioVenta();
        });

        btnMisOfertas.addActionListener(e -> {
            if (esInvitado())
                abrirLogin();
            else
                new MisOfertasView(pubController, usuarioLogueado).setVisible(true);
        });

        btnRefrescar.addActionListener(e -> cargarPublicaciones());

        btnVerDetalle.addActionListener(e -> verDetalleSeleccionado());
        btnEliminar.addActionListener(e -> eliminarPublicacionSeleccionada());
        btnEditar.addActionListener(e -> editarPublicacionSeleccionada());

        footer.add(btnVender);
        footer.add(btnMisOfertas);
        footer.add(btnRefrescar);
        footer.add(new JSeparator(SwingConstants.VERTICAL));
        footer.add(btnVerDetalle);
        footer.add(btnEditar);
        footer.add(btnEliminar);

        add(footer, BorderLayout.SOUTH);
    }

    // --- MÉTODOS LÓGICOS ---

    public void cargarPublicaciones() {
        listModel.clear();
        List<Publicacion> lista = pubController.obtenerPublicacionesActivas();

        if (lista.isEmpty()) {
            // No podemos agregar string al modelo de Publicacion, así que manejamos vacío
            // visualmente o nada
        } else {
            for (Publicacion p : lista) {
                listModel.addElement(p);
            }
        }
    }

    private void verDetalleSeleccionado() {
        if (esInvitado()) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión para ver detalles y ofertar.");
            return;
        }

        Publicacion seleccionada = listaVisual.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una publicación primero.");
            return;
        }

        new DetallePublicacionView(pubController, seleccionada, usuarioLogueado).setVisible(true);
    }

    private void eliminarPublicacionSeleccionada() {
        if (esInvitado()) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión.");
            return;
        }

        Publicacion seleccionada = listaVisual.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una publicación primero.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar '" + seleccionada.getTitulo() + "'?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = pubController.eliminarPublicacion(seleccionada.getIdArticulo(), usuarioLogueado.getId());
            if (exito) {
                JOptionPane.showMessageDialog(this, "Publicación eliminada.");
                cargarPublicaciones();
            } else {
                JOptionPane.showMessageDialog(this, "No puedes eliminar esta publicación (No eres el dueño).", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPublicacionSeleccionada() {
        if (esInvitado()) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión.");
            return;
        }

        Publicacion seleccionada = listaVisual.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una publicación primero.");
            return;
        }

        // Verificar dueño antes de abrir ventana
        if (!seleccionada.getIdVendedor().equals(usuarioLogueado.getId())) {
            JOptionPane.showMessageDialog(this, "No puedes editar esta publicación (No eres el dueño).", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abrir ventana de edición
        new EditarPublicacionView(pubController, usuarioLogueado, this, seleccionada).setVisible(true);
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

        // >>> CHAT - actualizar paneles de chat según usuario
        if (panelListaChats != null) {
            panelListaChats.setUsuarioActual(usuarioLogueado);
        }
        if (panelChatDetalle != null) {
            panelChatDetalle.setUsuarioActual(usuarioLogueado);
        }
        // <<< CHAT
    }
}
