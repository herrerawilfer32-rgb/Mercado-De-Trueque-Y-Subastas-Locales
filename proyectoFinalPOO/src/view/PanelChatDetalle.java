package view;

import controller.ChatController;
import model.User;
import model.chat.Chat;
import model.chat.Mensaje;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;

/**
 * Panel que muestra el detalle de un chat (conversación) y permite
 * enviar nuevos mensajes.
 *
 * Este panel depende de:
 *  - Un ChatController para la lógica de negocio.
 *  - Un User (usuarioActual) para saber quién envía los mensajes.
 *  - Un Chat (chatActual) que es la conversación que se está visualizando.
 */
public class PanelChatDetalle extends JPanel {

    // ---------------------------------------------------------
    // Atributos de negocio
    // ---------------------------------------------------------
    private ChatController chatController;
    private User usuarioActual;
    private Chat chatActual;

    // ---------------------------------------------------------
    // Componentes de la interfaz gráfica
    // ---------------------------------------------------------
    private JTextArea areaConversacion;
    private JTextField campoNuevoMensaje;
    private JButton botonEnviar;

    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructor principal del panel de detalle de chat.
     *
     * @param chatController Controlador de chat para gestionar el envío de mensajes.
     */
    public PanelChatDetalle(ChatController chatController) {
        if (chatController == null) {
            throw new IllegalArgumentException("El controlador de chat no puede ser nulo.");
        }
        this.chatController = chatController;

        inicializarComponentes();
        configurarEventos();
    }

    // ---------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------

    /**
     * Establece el usuario actual (logueado) que enviará los mensajes
     * desde este panel.
     *
     * @param usuarioActual Usuario logueado o null si se encuentra en modo invitado.
     */
    public void setUsuarioActual(User usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Establece el chat que se debe mostrar en el panel.
     * Al asignar un nuevo chat, se recarga el área de conversación
     * y se marca el chat como leído.
     *
     * @param chat Chat a visualizar.
     */
    public void setChatActual(Chat chat) {
        this.chatActual = chat;

        if (chatActual != null) {
            chatController.marcarChatComoLeido(chatActual);
        }

        recargarConversacion();
    }

    // ---------------------------------------------------------
    // Inicialización UI
    // ---------------------------------------------------------

    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de conversación: solo lectura, con salto de línea automático
        areaConversacion = new JTextArea();
        areaConversacion.setEditable(false);
        areaConversacion.setLineWrap(true);
        areaConversacion.setWrapStyleWord(true);

        JScrollPane scrollConversacion = new JScrollPane(areaConversacion);
        add(scrollConversacion, BorderLayout.CENTER);

        // Panel inferior: campo de texto + botón "Enviar"
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        campoNuevoMensaje = new JTextField();
        botonEnviar = new JButton("Enviar");

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(botonEnviar);

        panelInferior.add(campoNuevoMensaje, BorderLayout.CENTER);
        panelInferior.add(panelBoton, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        // Enviar al presionar el botón
        botonEnviar.addActionListener(e -> enviarMensajeDesdeCampo());

        // Enviar al presionar Enter en el campo de texto
        campoNuevoMensaje.addActionListener(e -> enviarMensajeDesdeCampo());
    }

    // ---------------------------------------------------------
    // Lógica de negocio de la vista
    // ---------------------------------------------------------

    /**
     * Envía el mensaje escrito en el campo de texto utilizando el controlador
     * de chat. Solo se envía si existe un chatActual y un usuarioActual.
     */
    private void enviarMensajeDesdeCampo() {
        if (chatActual == null) {
            return;
        }
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para enviar mensajes.",
                    "Sesión requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String texto = campoNuevoMensaje.getText();
        if (texto == null || texto.isBlank()) {
            return;
        }

        chatController.enviarMensaje(chatActual, usuarioActual, texto);
        campoNuevoMensaje.setText("");
        recargarConversacion();
    }

    /**
     * Recarga el área de conversación con todos los mensajes del chatActual.
     */
    private void recargarConversacion() {
        areaConversacion.setText("");

        if (chatActual == null) {
            return;
        }

        for (Mensaje mensaje : chatController.obtenerMensajes(chatActual)) {
            String nombre = mensaje.getUsuarioRemitente().getNombre();
            String hora = mensaje.getFechaHoraEnvio().format(formatoHora);
            String contenido = mensaje.getContenidoMensaje();

            areaConversacion.append(nombre + " (" + hora + "): " + contenido + "\n");
        }

        // Scroll automático al final
        areaConversacion.setCaretPosition(areaConversacion.getDocument().getLength());
    }
}
