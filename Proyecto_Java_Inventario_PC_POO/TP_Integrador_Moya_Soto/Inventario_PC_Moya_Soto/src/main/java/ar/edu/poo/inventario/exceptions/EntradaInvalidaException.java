package ar.edu.poo.inventario.exceptions;

/**
 * Excepcion personalizada para capturar y gestionar entradas de usuario no validas por consola
 * o violaciones de reglas de validacion de dominio (precios negativos, codigos vacios, etc.).
 */
public class EntradaInvalidaException extends Exception {
    private final String campoAfectado;

    public EntradaInvalidaException(String mensaje) {
        super(mensaje);
        this.campoAfectado = "General";
    }

    public EntradaInvalidaException(String campoAfectado, String mensaje) {
        super("Error en campo [" + campoAfectado + "]: " + mensaje);
        this.campoAfectado = campoAfectado;
    }

    public String getCampoAfectado() {
        return campoAfectado;
    }
}
