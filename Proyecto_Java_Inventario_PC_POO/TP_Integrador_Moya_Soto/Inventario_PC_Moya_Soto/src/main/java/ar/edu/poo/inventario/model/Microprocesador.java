package ar.edu.poo.inventario.model;

/**
 * Clase Concreta (Nivel 2 de herencia): Microprocesador (CPU).
 */
public class Microprocesador extends ComponenteHardware {
    private int cantidadNucleos;
    private double frecuenciaTurboGhz;
    private String socket;

    public Microprocesador(int id, String codigoSerie, String marca, String modelo,
                           double precioBase, String estadoOperativo,
                           String tipoInterfaz, int consumoWatts, int garantiaMeses,
                           int cantidadNucleos, double frecuenciaTurboGhz, String socket) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              tipoInterfaz, consumoWatts, garantiaMeses);
        this.cantidadNucleos = cantidadNucleos;
        this.frecuenciaTurboGhz = frecuenciaTurboGhz;
        this.socket = socket;
    }

    @Override
    public String getTipoCategoria() {
        return "COMP_CPU";
    }

    @Override
    public double calcularDepreciacion() {
        return getPrecioBase() * 0.10;
    }

    @Override
    public String ejecutarDiagnostico() {
        return String.format("Diagnostico CPU [%s]: %d nucleos @ %.2f GHz | Socket %s | TDP %dW | Pines/Pads OK",
                getCodigoSerie(), cantidadNucleos, frecuenciaTurboGhz, socket, getConsumoWatts());
    }

    @Override
    public boolean requiereMantenimientoUrgente() {
        return !"OPERATIVO".equalsIgnoreCase(getEstadoOperativo());
    }

    @Override
    public String obtenerFichaTecnica() {
        return String.format(
                "ID: %d | [%s] %s %s (S/N: %s)%n" +
                "   Arquitectura: %d Nucleos | Turbo %.2f GHz | Socket: %s | TDP: %dW | Garantia: %d meses%n" +
                "   Estado: %s | Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                cantidadNucleos, frecuenciaTurboGhz, socket, getConsumoWatts(), getGarantiaMeses(),
                getEstadoOperativo(), getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size()
        );
    }

    public int getCantidadNucleos() { return cantidadNucleos; }
    public void setCantidadNucleos(int cantidadNucleos) { this.cantidadNucleos = cantidadNucleos; }

    public double getFrecuenciaTurboGhz() { return frecuenciaTurboGhz; }
    public void setFrecuenciaTurboGhz(double frecuenciaTurboGhz) { this.frecuenciaTurboGhz = frecuenciaTurboGhz; }

    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }
}
