package ar.edu.poo.inventario.interfaces;

/**
 * Interfaz que define el contrato polimórfico para cualquier ítem registrable en el inventario.
 * Aplica el Principio de Segregación de Interfaces (ISP - SOLID).
 */
public interface Inventariable {
    String obtenerFichaTecnica();
    double calcularValorActual();
    String obtenerResumenEstado();
}
