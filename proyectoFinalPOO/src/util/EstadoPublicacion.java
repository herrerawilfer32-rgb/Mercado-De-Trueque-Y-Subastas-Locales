/**
 * Clase:  EstadoPublicacion
 *Enum que representa los diferentes estados en los que puede encontrarse una publicación dentro del sistema de ventas, trueques o subastas.
 * @author Anggel Leal, Wilfer Herrera, David Santos
 * @version 1.1
 */
package util;

public enum EstadoPublicacion {
	ACTIVA, // El articulo está en venta o subasta
	CERRADA, // La transacción ha finalizado
	FINALIZADA, // La transacción se completó exitosamente (pagada/intercambiada)
	PAUSADA, // El vendedor la retiró termporalmente (no sé si vamos a usar esto)
	ELIMINADA // El vendedor eliminó la publicación (solo puede ver el admin)
}
