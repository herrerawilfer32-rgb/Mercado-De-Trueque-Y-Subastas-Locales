package service;

import persistence.PublicacionRepository;
import model.Publicacion;
import model.User;

import java.util.List;

public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UserService userService;
    private final persistence.OfertaRepository ofertaRepository;

    public PublicacionService(PublicacionRepository publicacionRepository, UserService userService,
            persistence.OfertaRepository ofertaRepository) {
        this.publicacionRepository = publicacionRepository;
        this.userService = userService;
        this.ofertaRepository = ofertaRepository;
    }

    /*
     * Usando el UserService podemos obtener la ubicacion del vendedor
     */

    public String obtenerUbicacionVendedor(Publicacion publicacion) {
        // 1. Obtenemos el id del vendedor
        String idVendedor = publicacion.getIdVendedor();

        // 2. Usamos el userService para obtener el objeto User
        User vendedor = userService.buscarUsuarioPorId(idVendedor);

        // 3. Devolver la ubicación
        if (vendedor != null) {
            return vendedor.getUbicacion();
        }
        return "Ubicación no disponible";
    }

    // Buscar publicaciones por idVendedor
    public Publicacion buscarPublicacionPorId(String idPublicacion) {
        return publicacionRepository.buscarPorIdArticulo(idPublicacion);
    }

    /*
     * Cierra la publicación cambiando su estado a CERRADA
     */
    public void cerrarPublicacion(String idPublicacion) {
        // 1. Buscar la publicación por su ID
        Publicacion publicacion = publicacionRepository.buscarPorIdArticulo(idPublicacion);

        // 2. Validamos que exista
        if (publicacion != null) {
            // 3. Cambiamos el estado
            publicacion.setEstado(util.EstadoPublicacion.CERRADA);

            // 4. Guardamos el cambio (al ser un HashMap, el put sobrescribe y actualiza)
            publicacionRepository.guardar(publicacion);

            System.out.println("La publicación '" + publicacion.getTitulo() + "' ha sido CERRADA.");
        } else {
            System.out.println("Error: No se pudo cerrar la publicación. ID no encontrado.");
        }
    }

    /**
     * Devuelve todas las publicaciones activas.
     */
    public List<Publicacion> buscarPublicacionesActivas() {
        return publicacionRepository.buscarPublicacionesActivas();
    }

    /**
     * Guarda una publicación nueva (Subasta o Trueque).
     */
    public void guardarPublicacion(Publicacion publicacion) {
        publicacionRepository.guardar(publicacion);
    }

    /**
     * Elimina una publicación si el usuario solicitante es el dueño.
     */
    public boolean eliminarPublicacion(String idPublicacion, String idUsuarioSolicitante) {
        Publicacion pub = publicacionRepository.buscarPorIdArticulo(idPublicacion);
        if (pub != null && pub.getIdVendedor().equals(idUsuarioSolicitante)) {
            publicacionRepository.eliminar(idPublicacion);
            return true;
        }
        return false;
    }

    /**
     * Cierra una subasta y determina el ganador.
     */
    public void cerrarSubasta(String idPublicacion, String idVendedor) {
        Publicacion publicacion = publicacionRepository.buscarPorIdArticulo(idPublicacion);

        if (publicacion == null || !publicacion.getIdVendedor().equals(idVendedor)) {
            throw new IllegalArgumentException("Publicación no encontrada o no pertenece al vendedor.");
        }

        if (publicacion.getTipoPublicacion() != util.TipoPublicacion.SUBASTA) {
            throw new IllegalArgumentException("Esta publicación no es una subasta.");
        }

        // Buscar la mejor oferta
        List<model.Oferta> ofertas = ofertaRepository.buscarPorPublicacion(idPublicacion);
        model.Oferta mejorOferta = null;

        for (model.Oferta oferta : ofertas) {
            if (mejorOferta == null || oferta.getMontoOferta() > mejorOferta.getMontoOferta()) {
                mejorOferta = oferta;
            }
        }

        if (mejorOferta != null) {
            mejorOferta.setEstadoOferta(util.EstadoOferta.GANADORA);
            ofertaRepository.guardar(mejorOferta);
            System.out.println("Subasta cerrada. Ganador: " + mejorOferta.getIdOfertante());
        } else {
            System.out.println("Subasta cerrada sin ofertas.");
        }

        publicacion.setEstado(util.EstadoPublicacion.CERRADA);
        publicacionRepository.guardar(publicacion);
    }

    public List<Publicacion> recomendarTrueques(String idPublicacion) {
        Publicacion publicacion = publicacionRepository.buscarPorIdArticulo(idPublicacion);
        if (publicacion == null || publicacion.getTipoPublicacion() != util.TipoPublicacion.TRUEQUE) {
            return java.util.Collections.emptyList();
        }

        model.PublicacionTrueque trueque = (model.PublicacionTrueque) publicacion;
        String deseos = trueque.getObjetosDeseados().toLowerCase();

        // Búsqueda simple: buscar publicaciones activas cuyo título contenga alguna
        // palabra de los deseos
        List<Publicacion> activas = publicacionRepository.buscarPublicacionesActivas();
        List<Publicacion> recomendaciones = new java.util.ArrayList<>();

        for (Publicacion p : activas) {
            if (p.getIdArticulo().equals(idPublicacion))
                continue; // No recomendarse a sí mismo

            if (deseos.contains(p.getTitulo().toLowerCase())) {
                recomendaciones.add(p);
            }
        }

        return recomendaciones;
    }

    /**
     * Actualiza una publicación si el usuario solicitante es el dueño.
     */
    public boolean actualizarPublicacion(Publicacion publicacion, String idUsuarioSolicitante) {
        // Verificamos que la publicación exista y el usuario sea el dueño
        Publicacion pubExistente = publicacionRepository.buscarPorIdArticulo(publicacion.getIdArticulo());

        if (pubExistente != null && pubExistente.getIdVendedor().equals(idUsuarioSolicitante)) {
            // Mantenemos la fecha de publicación original si se desea, o actualizamos todo
            // Aquí simplemente guardamos la nueva versión que ya debe traer los cambios
            publicacionRepository.guardar(publicacion);
            return true;
        }
        return false;
    }
    /**
     * Elimina una publicación por su identificador.
     *
     * @param idPublicacion Identificador de la publicación a eliminar.
     */
    public void eliminar(String idPublicacion) {
        if (idPublicacion == null) {
            return;
        }
        baseDeDatos.remove(idPublicacion);
}
