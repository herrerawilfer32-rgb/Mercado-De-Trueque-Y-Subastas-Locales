package model.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.User;

/**
 * Representa una conversación (chat) entre dos usuarios dentro de la plataforma.
 * Contiene la lista de mensajes intercambiados y banderas de estado 
 * como la existencia de mensajes no leídos.
 */
public class Chat {
    
    // Atributos principales
    private String identificadorChat;
    private User usuarioEmisor;
    private User usuarioReceptor;
    private List<Mensaje> listaMensajes;
    private boolean tieneMensajesNoLeidos;
    
    /**
     * Constructor principal de la clase Chat.
     *
     * @param identificadorChat Identificador único del chat.
     * @param usuarioEmisor Usuario que inicia o crea el chat.
     * @param usuarioReceptor Usuario con quien se establece la conversación.
     */
    public Chat(String identificadorChat,
                User usuarioEmisor,
                User usuarioReceptor) {
        
        if (identificadorChat == null || identificadorChat.isBlank()) {
            throw new IllegalArgumentException("El identificador del chat no puede ser nulo ni vacío.");
        }
        if (usuarioEmisor == null || usuarioReceptor == null) {
            throw new IllegalArgumentException("Los usuarios del chat no pueden ser nulos.");
        }
        if (usuarioEmisor.equals(usuarioReceptor)) {
            throw new IllegalArgumentException("Un chat debe ser entre dos usuarios diferentes.");
        }
        
        this.identificadorChat = identificadorChat.trim();
        this.usuarioEmisor = usuarioEmisor;
        this.usuarioReceptor = usuarioReceptor;
        this.listaMensajes = new ArrayList<>();
        this.tieneMensajesNoLeidos = false;
    }
    
    // Métodos públicos de negocio
    
    /**
     * Agrega un nuevo mensaje al chat.
     * Marca el chat como que tiene mensajes no leídos.
     *
     * @param mensaje Mensaje a agregar al chat.
     */
    public void agregarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            return;
        }
        this.listaMensajes.add(mensaje);
        this.tieneMensajesNoLeidos = true;
    }
    
    /**
     * Marca el chat como leído, reseteando la bandera de mensajes no leídos.
     */
    public void marcarMensajesComoLeidos() {
        this.tieneMensajesNoLeidos = false;
    }
    
    /**
     * Obtiene una copia inmutable de la lista de mensajes del chat.
     *
     * @return Lista de mensajes (no modificable externamente).
     */
    public List<Mensaje> getListaMensajes() {
        return Collections.unmodifiableList(listaMensajes);
    }
    
    // Getters y setters básicos
    
    public String getIdentificadorChat() {
        return identificadorChat;
    }
    
    public User getUsuarioEmisor() {
        return usuarioEmisor;
    }
    
    public User getUsuarioReceptor() {
        return usuarioReceptor;
    }
    
    public boolean isTieneMensajesNoLeidos() {
        return tieneMensajesNoLeidos;
    }
}
