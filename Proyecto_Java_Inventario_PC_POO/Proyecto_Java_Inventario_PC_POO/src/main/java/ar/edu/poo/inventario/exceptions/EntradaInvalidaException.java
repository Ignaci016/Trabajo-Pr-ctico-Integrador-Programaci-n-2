package ar.edu.poo.inventario.exceptions;

/**
 * Excepción personalizada para capturar y gestionar entradas de usuario no válidas por consola
 * o violaciones de reglas de validación de dominio (precios negativos, códigos vacíos, etc.).
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
