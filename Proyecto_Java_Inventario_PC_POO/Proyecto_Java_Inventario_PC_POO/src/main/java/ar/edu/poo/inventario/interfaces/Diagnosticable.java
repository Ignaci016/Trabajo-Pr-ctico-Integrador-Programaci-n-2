package ar.edu.poo.inventario.interfaces;

/**
 * Interfaz que permite ejecutar diagnósticos técnicos polimórficos sobre PCs y componentes.
 * Extiende el comportamiento de las clases implementando diferentes criterios de chequeo.
 */
public interface Diagnosticable {
    String ejecutarDiagnostico();
    boolean requiereMantenimientoUrgente();
}
