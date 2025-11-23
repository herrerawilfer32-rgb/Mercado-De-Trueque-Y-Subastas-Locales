package persistence;

import java.util.ArrayList;
import java.util.List;

import model.User;
import model.chat.Chat;

/**
 * Implementación básica del repositorio de chats utilizando una lista en memoria.
 * 
 * Posteriormente se puede extender para leer y escribir en archivos, manteniendo
 * la misma interfaz ChatRepository, sin afectar la lógica de negocio.
 */
public class ChatFileRepository implements ChatRepository {
    
    // Lista interna que simula la persistencia
    private final List<Chat> listaChats;
    
    /**
     * Constructor principal. Inicializa la estructura interna.
     */
    public ChatFileRepository() {
        this.listaChats = new ArrayList<>();
    }
    
    @Override
    public synchronized void guardarChat(Chat chat) {
        if (chat == null) {
            return;
        }
        
        // Verificar si ya existe un chat con el mismo identificador
        Chat chatExistente = buscarChatPorIdentificador(chat.getIdentificadorChat());
        
        if (chatExistente == null) {
            listaChats.add(chat);
        }
        // Si ya existe, no se hace nada: el objeto ya se modificó en memoria
        
        // TODO: Persistir información en archivo (lectura/escritura)
    }
    
    @Override
    public synchronized Chat buscarChatEntreUsuarios(User usuarioA, User usuarioB) {
        if (usuarioA == null || usuarioB == null) {
            return null;
        }
        
        for (Chat chat : listaChats) {
            boolean coincideMismaPareja =
                    (chat.getUsuarioEmisor().equals(usuarioA) && chat.getUsuarioReceptor().equals(usuarioB))
                    || (chat.getUsuarioEmisor().equals(usuarioB) && chat.getUsuarioReceptor().equals(usuarioA));
            if (coincideMismaPareja) {
                return chat;
            }
        }
        
        return null;
    }
    
    @Override
    public synchronized List<Chat> listarChatsDeUsuario(User usuario) {
        List<Chat> resultado = new ArrayList<>();
        
        if (usuario == null) {
            return resultado;
        }
        
        for (Chat chat : listaChats) {
            boolean usuarioParticipa =
                    chat.getUsuarioEmisor().equals(usuario) || chat.getUsuarioReceptor().equals(usuario);
            if (usuarioParticipa) {
                resultado.add(chat);
            }
        }
        
        return resultado;
    }
    
    /**
     * Método privado de apoyo para buscar un chat por su identificador.
     *
     * @param identificadorChat Identificador único del chat.
     * @return Chat encontrado o null si no existe.
     */
    private Chat buscarChatPorIdentificador(String identificadorChat) {
        if (identificadorChat == null || identificadorChat.isBlank()) {
            return null;
        }
        
        for (Chat chat : listaChats) {
            if (chat.getIdentificadorChat().equals(identificadorChat)) {
                return chat;
            }
        }
        return null;
    }
    
    /**
     * Retorna una copia de todos los chats.
     * Útil para depuración o funciones administrativas.
     */
    public List<Chat> obtenerTodos() {
        return new ArrayList<>(listaChats);
    }
}
