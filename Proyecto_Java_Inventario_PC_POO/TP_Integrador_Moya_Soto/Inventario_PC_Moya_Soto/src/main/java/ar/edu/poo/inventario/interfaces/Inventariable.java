package ar.edu.poo.inventario.interfaces;

/**
 * Interfaz que define el contrato polimorfico para cualquier item registrable en el inventario.
 * Aplica el Principio de Segregacion de Interfaces (ISP - SOLID).
 */
public interface Inventariable {
    String obtenerFichaTecnica();
    double calcularValorActual();
    String obtenerResumenEstado();
}
