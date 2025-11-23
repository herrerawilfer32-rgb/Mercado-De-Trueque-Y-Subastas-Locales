package view;

import controller.ChatController;
import model.User;
import model.chat.Chat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/**
 * Panel encargado de mostrar la lista de chats asociados a un usuario.
 * 
 * Este panel no abre ventanas por sí mismo, sino que notifica a un
 * componente externo mediante un listener cuando se desea abrir un chat.
 */
public class PanelListaChats extends JPanel {

    /**
     * Listener para notificar que el usuario desea abrir un chat específico.
     */
    public interface ChatSeleccionListener {
        void abrirChat(Chat chatSeleccionado);
    }

    // ---------------------------------------------------------
    // Atributos de negocio
    // ---------------------------------------------------------
    private ChatController chatController;
    private User usuarioActual;
    private ChatSeleccionListener chatSeleccionListener;

    // ---------------------------------------------------------
    // Componentes de la interfaz gráfica
    // ---------------------------------------------------------
    private JTable tablaChats;
    private DefaultTableModel modeloTablaChats;
    private JButton botonAbrirChat;
    private JButton botonActualizar;
    private JLabel etiquetaUsuarioActual;

    /**
     * Constructor principal del panel de lista de chats.
     *
     * @param chatController        Controlador de chat que provee los datos.
     * @param chatSeleccionListener Listener que será notificado cuando se
     *                              desee abrir un chat específico.
     */
    public PanelListaChats(ChatController chatController,
                           ChatSeleccionListener chatSeleccionListener) {

        if (chatController == null) {
            throw new IllegalArgumentException("El controlador de chat no puede ser nulo.");
        }

        this.chatController = chatController;
        this.chatSeleccionListener = chatSeleccionListener;

        inicializarComponentes();
        configurarEventos();
        // No se cargan chats aún, porque puede que no haya usuario logueado
        actualizarEtiquetaUsuario();
    }

    // ---------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------

    /**
     * Establece el usuario actual (logueado) sobre el cual se listarán los chats.
     * <p>
     * Si el usuario es null, el panel entra en modo invitado y no mostrará chats.
     *
     * @param usuarioActual Usuario logueado o null si es invitado.
     */
    public void setUsuarioActual(User usuarioActual) {
        this.usuarioActual = usuarioActual;
        actualizarEtiquetaUsuario();
        cargarChatsEnTabla();
    }

    // ---------------------------------------------------------
    // Métodos privados de inicialización y UI
    // ---------------------------------------------------------

    /**
     * Inicializa y organiza los componentes gráficos del panel.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Panel superior: info de usuario + botón actualizar ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        etiquetaUsuarioActual = new JLabel();
        panelSuperior.add(etiquetaUsuarioActual, BorderLayout.WEST);

        botonActualizar = new JButton("Actualizar");
        JPanel panelBotonActualizar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonActualizar.add(botonActualizar);
        panelSuperior.add(panelBotonActualizar, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // ---- Tabla de chats ----
        modeloTablaChats = new DefaultTableModel(
                new Object[]{"Contacto", "Mensajes no leídos", "Total mensajes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // La tabla es de solo lectura
                return false;
            }
        };

        tablaChats = new JTable(modeloTablaChats);
        tablaChats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaChats);
        add(scrollTabla, BorderLayout.CENTER);

        // ---- Panel inferior: botón "Abrir chat" ----
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonAbrirChat = new JButton("Abrir chat");
        panelInferior.add(botonAbrirChat);

        add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Configura los manejadores de eventos de los botones.
     */
    private void configurarEventos() {
        botonActualizar.addActionListener(e -> cargarChatsEnTabla());

        botonAbrirChat.addActionListener(e -> notificarChatSeleccionado());
    }

    /**
     * Actualiza el texto de la etiqueta que indica el usuario actual.
     */
    private void actualizarEtiquetaUsuario() {
        if (usuarioActual == null) {
            etiquetaUsuarioActual.setText("Chats de: Invitado (sin sesión)");
        } else {
            etiquetaUsuarioActual.setText("Chats de: " + usuarioActual.getNombre());
        }
    }

    /**
     * Carga en la tabla los chats asociados al usuario actual.
     * Si no hay usuario logueado, la tabla se limpia.
     */
    private void cargarChatsEnTabla() {
        modeloTablaChats.setRowCount(0);

        if (usuarioActual == null) {
            // Modo invitado: no existe lista de chats
            return;
        }

        List<Chat> listaChats = chatController.listarChatsDeUsuario(usuarioActual);
        if (listaChats == null || listaChats.isEmpty()) {
            return;
        }

        for (Chat chat : listaChats) {
            User otroUsuario = obtenerOtroUsuarioDelChat(chat);

            String nombreContacto = (otroUsuario != null)
                    ? otroUsuario.getNombre()
                    : "Desconocido";

            String textoNoLeidos = chat.isTieneMensajesNoLeidos() ? "Sí" : "No";
            int totalMensajes = chat.getListaMensajes().size();

            modeloTablaChats.addRow(new Object[]{
                    nombreContacto,
                    textoNoLeidos,
                    totalMensajes
            });
        }
    }

    /**
     * Determina el otro usuario participante del chat, diferente al usuarioActual.
     *
     * @param chat Chat del cual se desea conocer el otro participante.
     * @return Usuario contrario al usuarioActual, o null si no se puede determinar.
     */
    private User obtenerOtroUsuarioDelChat(Chat chat) {
        if (chat == null || usuarioActual == null) {
            return null;
        }

        if (usuarioActual.equals(chat.getUsuarioEmisor())) {
            return chat.getUsuarioReceptor();
        }
        if (usuarioActual.equals(chat.getUsuarioReceptor())) {
            return chat.getUsuarioEmisor();
        }
        return null;
    }

    /**
     * Obtiene el chat correspondiente a la fila seleccionada y notifica
     * al listener externo para que este decida cómo abrirlo.
     */
    private void notificarChatSeleccionado() {
        if (chatSeleccionListener == null) {
            return;
        }
        if (usuarioActual == null) {
            // Sin usuario no tiene sentido abrir chats
            return;
        }

        int filaSeleccionada = tablaChats.getSelectedRow();
        if (filaSeleccionada < 0) {
            return;
        }

        List<Chat> listaChats = chatController.listarChatsDeUsuario(usuarioActual);
        if (listaChats == null || listaChats.isEmpty()) {
            return;
        }
        if (filaSeleccionada >= listaChats.size()) {
            return;
        }

        Chat chatSeleccionado = listaChats.get(filaSeleccionada);
        chatSeleccionListener.abrirChat(chatSeleccionado);
    }
}
