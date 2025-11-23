package service;

import java.util.Date;
import java.util.List;

import persistence.OfertaRepository;
import model.Oferta;
import model.Publicacion;
import model.User;
import util.EstadoOferta;
import util.TipoPublicacion;

public class OfertaService {

    private final OfertaRepository ofertaRepository;
    private final UserService userService;
    private final PublicacionService publicacionService;

    public OfertaService(OfertaRepository ofertaRepository, UserService userService,
            PublicacionService publicacionService) {
        this.ofertaRepository = ofertaRepository;
        this.userService = userService;
        this.publicacionService = publicacionService;
    }

    public Oferta realizarNuevaOferta(String idOferta, String idPublicacion, String idOfertante, Date fechaOferta,
            double montoOferta, String descripcionTrueque) {
        // 1. Validar que el ofertante exista
        User ofertante = userService.buscarUsuarioPorId(idOfertante);
        if (ofertante == null) {
            System.out.println("Error: El ofertante con ID " + idOfertante + " no existe.");
            return null;
        }

        // 2. Validar que la publicación exista
        Publicacion publicacion = publicacionService.buscarPublicacionPorId(idPublicacion);
        if (publicacion == null) {
            System.out.println("Error: La publicación con ID " + idPublicacion + " no existe.");
            return null;
        }
        // 3. Validar que la publicación esté activa
        if (publicacion.getEstado() != util.EstadoPublicacion.ACTIVA) {
            System.out.println("Error: La publicación con ID " + idPublicacion + " no está activa.");
            return null;
        }
        // Diferenciar entre subasta y trueque
        // SUBASTA
        if (publicacion.getTipoPublicacion() == TipoPublicacion.SUBASTA) {

            // Tiene que haber un monto si o si
            if (montoOferta <= 0) {
                throw new RuntimeException("Error: En una subasta el monto debe ser positivo.");
            }

            // Validar que sea un monto superior a la mayor oferta
            // Traemos todas las ofertas de la publicacion
            List<Oferta> ofertasPorPublicacion = ofertaRepository.buscarPorPublicacion(idPublicacion);
            double mayorOferta = 0;
            // Iteramos en las ofertas para encontrar la mayor
            for (Oferta oferta : ofertasPorPublicacion) {
                if (oferta.getMontoOferta() > mayorOferta) {
                    mayorOferta = oferta.getMontoOferta();
                }
            }

            if (montoOferta <= mayorOferta) {
                throw new RuntimeException(
                        "Error: El monto de la oferta debe ser superior a la mayor oferta actual de " + mayorOferta);
            }
        }

        // TRUEQUE
        if (publicacion.getTipoPublicacion() == TipoPublicacion.TRUEQUE) {
            // Tiene que haber una descripcion del trueque
            if (descripcionTrueque == null || descripcionTrueque.isEmpty()) {
                throw new RuntimeException("Error: En un trueque la descripción del trueque no puede estar vacía.");
            }
        }

        // 4. Crear la nueva oferta
        Oferta nuevaOferta = new Oferta(idOferta, idPublicacion, idOfertante, new java.util.Date(), montoOferta,
                descripcionTrueque, EstadoOferta.PENDIENTE);

        // 4. Guardar la oferta en el repositorio
        ofertaRepository.guardar(nuevaOferta);

        System.out.println("Oferta realizada exitosamente por el usuario " + ofertante.getNombre()
                + " en la publicación " + publicacion.getTitulo() + ".");
        return nuevaOferta;
    }

    // Método para aceptar una oferta
    public boolean aceptarOferta(String idOferta, String idVendedorQueAcepta) {
        // Buscar la oferta
        Oferta oferta = ofertaRepository.buscarPorIdOferta(idOferta);
        // 1. Validar que la oferta exista.
        if (oferta == null) {
            System.out.println("Error: La oferta con ID " + idOferta + " no existe.");
            return false;
        }

        // 2. Buscar la publicación asociada a la oferta.
        Publicacion publicacion = publicacionService.buscarPublicacionPorId(oferta.getIdPublicacion());

        // 3Validamos que quien intenta aceptar la oferta sea el dueño de la publicación
        if (!publicacion.getIdVendedor().equals(idVendedorQueAcepta)) {
            System.out.println("Error: Solo el dueño de la publicación puede aceptar una oferta.");
            return false;
        }

        // 4. Validamos que la publicación siga abierta.
        if (publicacion.getEstado() != util.EstadoPublicacion.ACTIVA) {
            System.out.println("Error: La publicación con ID " + publicacion.getIdArticulo() + " no está activa.");
            return false;
        }

        // 5. LOGICA DE CIERRE

        // A. Cambiamos estado de la oferta a ACEPTADA
        oferta.setEstadoOferta(EstadoOferta.ACEPTADA);
        ofertaRepository.guardar(oferta); // Actualizamos en BD

        // B. Cerramos la publicación
        publicacionService.cerrarPublicacion(publicacion.getIdArticulo());

        System.out.println("¡Felicidades! Oferta aceptada. La publicación ha sido cerrada.");
        return true;
    }

}
