package model.chat;

import java.time.LocalDateTime;

import model.User;

/**
 * Representa un mensaje enviado dentro de un chat entre dos usuarios.
 * La clase es inmutable una vez creada, garantizando consistencia de datos.
 */
public class Mensaje {
    
    // Atributos principales
    private final String identificadorMensaje;
    private final User usuarioRemitente;
    private final String contenidoMensaje;
    private final LocalDateTime fechaHoraEnvio;
    
    /**
     * Constructor principal de la clase Mensaje.
     * 
     * Se asume que las validaciones de negocio se realizan previamente
     * en los controladores. Aquí solo se aplican validaciones mínimas
     * para garantizar que el objeto no quede en un estado inconsistente.
     *
     * @param identificadorMensaje Identificador único del mensaje.
     * @param usuarioRemitente Usuario que envía el mensaje.
     * @param contenidoMensaje Contenido textual del mensaje.
     * @param fechaHoraEnvio Fecha y hora en la que se envía el mensaje.
     */
    public Mensaje(String identificadorMensaje,
                   User usuarioRemitente,
                   String contenidoMensaje,
                   LocalDateTime fechaHoraEnvio) {
        
        // Validaciones mínimas para evitar estados inválidos
        if (identificadorMensaje == null || identificadorMensaje.isBlank()) {
            throw new IllegalArgumentException("El identificador del mensaje no puede ser nulo ni vacío.");
        }
        if (usuarioRemitente == null) {
            throw new IllegalArgumentException("El usuario remitente no puede ser nulo.");
        }
        if (contenidoMensaje == null || contenidoMensaje.isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje no puede ser nulo ni vacío.");
        }
        if (fechaHoraEnvio == null) {
            throw new IllegalArgumentException("La fecha y hora de envío no pueden ser nulas.");
        }
        
        this.identificadorMensaje = identificadorMensaje.trim();
        this.usuarioRemitente = usuarioRemitente;
        this.contenidoMensaje = contenidoMensaje.trim();
        this.fechaHoraEnvio = fechaHoraEnvio;
    }
    
    // Métodos públicos de acceso (getters)
    
    /**
     * Obtiene el identificador único del mensaje.
     * 
     * @return Identificador del mensaje.
     */
    public String getIdentificadorMensaje() {
        return identificadorMensaje;
    }
    
    /**
     * Obtiene el usuario que envió el mensaje.
     * 
     * @return Usuario remitente.
     */
    public User getUsuarioRemitente() {
        return usuarioRemitente;
    }
    
    /**
     * Obtiene el contenido textual del mensaje.
     * 
     * @return Contenido del mensaje.
     */
    public String getContenidoMensaje() {
        return contenidoMensaje;
    }
    
    /**
     * Obtiene la fecha y hora en que se envió el mensaje.
     * 
     * @return Fecha y hora de envío.
     */
    public LocalDateTime getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }
}
