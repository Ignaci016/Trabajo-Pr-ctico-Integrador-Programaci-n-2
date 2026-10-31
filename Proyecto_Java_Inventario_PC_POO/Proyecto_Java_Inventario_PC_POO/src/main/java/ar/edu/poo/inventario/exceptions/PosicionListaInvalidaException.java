package ar.edu.poo.inventario.exceptions;

/**
 * Excepción personalizada para controlar intentos de acceso a posiciones inválidas
 * dentro de listas de productos, componentes o historiales de revisiones técnicas.
 */
public class PosicionListaInvalidaException extends Exception {
    private final int indiceIntentado;
    private final int tamanoActual;

    public PosicionListaInvalidaException(int indiceIntentado, int tamanoActual) {
        super(String.format(
            "Posición de lista inválida: se intentó acceder al índice %d, pero el rango válido es [0 .. %d] (total elementos: %d).",
            indiceIntentado, Math.max(0, tamanoActual - 1), tamanoActual
        ));
        this.indiceIntentado = indiceIntentado;
        this.tamanoActual = tamanoActual;
    }

    public int getIndiceIntentado() {
        return indiceIntentado;
    }

    public int getTamanoActual() {
        return tamanoActual;
    }
}
