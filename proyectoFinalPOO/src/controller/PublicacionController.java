package controller;

import model.Publicacion;
import model.PublicacionSubasta;
import model.PublicacionTrueque;
import service.PublicacionService;
import service.OfertaService;
import model.User;
import model.Oferta;

import java.util.Date;
import java.util.List;

public class PublicacionController {

    private final PublicacionService publicacionService;
    private final OfertaService ofertaService;

    public PublicacionController(PublicacionService publicacionService, OfertaService ofertaService) {
        this.publicacionService = publicacionService;
        this.ofertaService = ofertaService;
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

    // --- Métodos para Ofertas ---

    public boolean ofertar(String idPublicacion, String idOfertante, double monto, String descripcionTrueque) {
        try {
            ofertaService.realizarNuevaOferta(generarIdOferta(), idPublicacion, idOfertante, new Date(), monto,
                    descripcionTrueque);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al ofertar: " + e.toString());
            return false;
        }
    }

    public List<Oferta> obtenerOfertas(String idPublicacion) {
        return ofertaService.obtenerOfertasPorPublicacion(idPublicacion);
    }

    public boolean aceptarOferta(String idOferta, String idVendedor) {
        try {
            return ofertaService.aceptarOferta(idOferta, idVendedor);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    public boolean rechazarOferta(String idOferta, String idVendedor) {
        try {
            return ofertaService.rechazarOferta(idOferta, idVendedor);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    public boolean eliminarOferta(String idOferta, String idSolicitante) {
        try {
            return ofertaService.eliminarOferta(idOferta, idSolicitante);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    public void cerrarSubasta(String idPublicacion, String idVendedor) {
        try {
            publicacionService.cerrarSubasta(idPublicacion, idVendedor);
            javax.swing.JOptionPane.showMessageDialog(null, "Subasta cerrada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al cerrar subasta: " + e.toString());
        }
    }

    private String generarId() {
        return "PUB-" + System.currentTimeMillis();
    }

    private String generarIdOferta() {
        return "OFE-" + System.currentTimeMillis();
    }
}