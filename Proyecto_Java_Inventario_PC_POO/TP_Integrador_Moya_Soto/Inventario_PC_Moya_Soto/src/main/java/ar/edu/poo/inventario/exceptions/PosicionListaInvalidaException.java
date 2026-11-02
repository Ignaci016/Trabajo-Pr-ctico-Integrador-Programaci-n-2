package ar.edu.poo.inventario.exceptions;

/**
 * Excepcion personalizada para controlar intentos de acceso a posiciones invalidas
 * dentro de listas de productos, componentes o historiales de revisiones tecnicas.
 */
public class PosicionListaInvalidaException extends Exception {
    private final int indiceIntentado;
    private final int tamanoActual;

    public PosicionListaInvalidaException(int indiceIntentado, int tamanoActual) {
        super(String.format(
            "Posicion de lista invalida: se intento acceder al indice %d, pero el rango valido es [0 .. %d] (total elementos: %d).",
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
