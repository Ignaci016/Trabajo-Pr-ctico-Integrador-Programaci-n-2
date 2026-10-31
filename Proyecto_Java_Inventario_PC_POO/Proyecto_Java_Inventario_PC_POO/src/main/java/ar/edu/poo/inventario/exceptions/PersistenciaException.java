package ar.edu.poo.inventario.exceptions;

/**
 * Excepción personalizada para encapsular errores de acceso a la base de datos SQLite.
 */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
}
