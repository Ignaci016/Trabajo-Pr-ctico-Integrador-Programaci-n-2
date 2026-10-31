package ar.edu.poo.inventario.model;

/**
 * Clase Concreta (Nivel 2 de herencia): Unidad de Almacenamiento (NVMe / SSD / HDD).
 */
public class DiscoAlmacenamiento extends ComponenteHardware {
    private int capacidadGb;
    private String tipoTecnologia; // NVMe M.2, SSD SATA, HDD
    private int saludSmartPorcentaje;

    public DiscoAlmacenamiento(int id, String codigoSerie, String marca, String modelo,
                               double precioBase, String estadoOperativo,
                               String tipoInterfaz, int consumoWatts, int garantiaMeses,
                               int capacidadGb, String tipoTecnologia, int saludSmartPorcentaje) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              tipoInterfaz, consumoWatts, garantiaMeses);
        this.capacidadGb = capacidadGb;
        this.tipoTecnologia = tipoTecnologia;
        this.saludSmartPorcentaje = saludSmartPorcentaje;
    }

    @Override
    public String getTipoCategoria() {
        return "COMP_DISCO";
    }

    @Override
    public double calcularDepreciacion() {
        double desgasteSmart = (100 - saludSmartPorcentaje) / 100.0;
        return getPrecioBase() * Math.min(0.80, 0.10 + desgasteSmart);
    }

    @Override
    public String ejecutarDiagnostico() {
        String estadoSmart = (saludSmartPorcentaje < 75)
                ? "ADVERTENCIA S.M.A.R.T.: degradación de celdas/sectores (" + saludSmartPorcentaje + "%)"
                : "S.M.A.R.T. Óptimo (" + saludSmartPorcentaje + "% vida útil restante)";
        return String.format("Diagnóstico Almacenamiento [%s]: %d GB %s (%s) | %s",
                getCodigoSerie(), capacidadGb, tipoTecnologia, getTipoInterfaz(), estadoSmart);
    }

    @Override
    public boolean requiereMantenimientoUrgente() {
        return !"OPERATIVO".equalsIgnoreCase(getEstadoOperativo()) || saludSmartPorcentaje < 75;
    }

    @Override
    public String obtenerFichaTecnica() {
        return String.format(
                "ID: %d | [%s] %s %s (S/N: %s)%n" +
                "   Unidad: %d GB (%s - %s) | Salud S.M.A.R.T.: %d%% | Garantía: %d meses%n" +
                "   Estado: %s | Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                capacidadGb, tipoTecnologia, getTipoInterfaz(), saludSmartPorcentaje, getGarantiaMeses(),
                getEstadoOperativo(), getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size()
        );
    }

    public int getCapacidadGb() { return capacidadGb; }
    public void setCapacidadGb(int capacidadGb) { this.capacidadGb = capacidadGb; }

    public String getTipoTecnologia() { return tipoTecnologia; }
    public void setTipoTecnologia(String tipoTecnologia) { this.tipoTecnologia = tipoTecnologia; }

    public int getSaludSmartPorcentaje() { return saludSmartPorcentaje; }
    public void setSaludSmartPorcentaje(int saludSmartPorcentaje) { this.saludSmartPorcentaje = saludSmartPorcentaje; }
}
