package controller;

import model.Publicacion;
import model.PublicacionSubasta;
import model.PublicacionTrueque;
import service.PublicacionService;
import model.User;

import java.util.Date;
import java.util.List;

public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    public List<Publicacion> obtenerPublicacionesActivas() {
        return publicacionService.buscarPublicacionesActivas();
    }

    public boolean crearSubasta(String titulo, String descripcion, User vendedor, double precioMinimo,
            int diasDuracion, List<String> fotosPaths) {
        try {
            Date fechaVencimiento = new Date(System.currentTimeMillis() + (diasDuracion * 86400000L));

            PublicacionSubasta subasta = new PublicacionSubasta(
                    generarId(), titulo, descripcion, vendedor.getId(), fotosPaths, precioMinimo, fechaVencimiento,
                    fechaVencimiento);

            publicacionService.guardarPublicacion(subasta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean crearTrueque(String titulo, String descripcion, User vendedor, String objetosDeseados,
            List<String> fotosPaths) {
        try {
            PublicacionTrueque trueque = new PublicacionTrueque(
                    generarId(), titulo, descripcion, vendedor.getId(), fotosPaths, objetosDeseados);

            publicacionService.guardarPublicacion(trueque);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPublicacion(String idPublicacion, String idUsuarioSolicitante) {
        return publicacionService.eliminarPublicacion(idPublicacion, idUsuarioSolicitante);
    }

    public boolean actualizarPublicacion(Publicacion publicacion, String idUsuarioSolicitante) {
        return publicacionService.actualizarPublicacion(publicacion, idUsuarioSolicitante);
    }

    private String generarId() {
        return "PUB-" + System.currentTimeMillis();
    }
}