package controller;

import model.Publicacion;
import model.PublicacionSubasta;
import model.PublicacionTrueque;
import model.PublicacionService;
import model.User;

import java.util.Date;
import java.util.List;

public class PublicacionController {

    private final PublicacionService publicacionService;

    // Este es el constructor que MainApp está buscando y no encuentra
    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    // Este es el método que MainWindow está buscando
    public List<Publicacion> obtenerPublicacionesActivas() {
        return publicacionService.buscarPublicacionesActivas();
    }

    // Este es el método que CrearPublicacionView está buscando
    public boolean crearSubasta(String titulo, String descripcion, User vendedor, double precioMinimo, int diasDuracion) {
        try {
            // Calculamos fecha de vencimiento (fecha actual + días de duración)
            Date fechaVencimiento = new Date(System.currentTimeMillis() + (diasDuracion * 86400000L));
            
            // Creamos el objeto Subasta
            // Nota: Pasamos 'null' en fotos por ahora para simplificar
            PublicacionSubasta subasta = new PublicacionSubasta(
                generarId(), titulo, descripcion, vendedor.getId(), null, precioMinimo, fechaVencimiento, fechaVencimiento
            );
            
            // Llamamos al servicio para guardar
            publicacionService.guardarPublicacion(subasta); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean crearTrueque(String titulo, String descripcion, User vendedor, String objetosDeseados) {
        try {
            // Creamos el objeto Trueque
            PublicacionTrueque trueque = new PublicacionTrueque(
                generarId(), titulo, descripcion, vendedor.getId(), null, objetosDeseados
            );
            
            publicacionService.guardarPublicacion(trueque);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Método auxiliar para generar un ID simple (en un sistema real usaríamos UUID)
    private String generarId() {
        return "PUB-" + System.currentTimeMillis();
    }
}