package ar.edu.poo.inventario.model;

/**
 * Clase Concreta (Nivel 2 de herencia): PC de Escritorio / Workstation.
 */
public class PcEscritorio extends EquipoComputo {
    private int wattsFuente;
    private String factorFormaGabinete; // ATX, Micro-ATX, Mini-ITX
    private boolean tieneGpuDedicada;

    public PcEscritorio(int id, String codigoSerie, String marca, String modelo,
                        double precioBase, String estadoOperativo,
                        String procesador, int memoriaRamGb, int almacenamientoGb,
                        int wattsFuente, String factorFormaGabinete, boolean tieneGpuDedicada) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              procesador, memoriaRamGb, almacenamientoGb);
        this.wattsFuente = wattsFuente;
        this.factorFormaGabinete = factorFormaGabinete;
        this.tieneGpuDedicada = tieneGpuDedicada;
    }

    @Override
    public String getTipoCategoria() {
        return "PC_ESCRITORIO";
    }

    @Override
    public double calcularDepreciacion() {
        // Las PCs de escritorio se deprecian un 12% base o 25% si están en revisión/defectuosas
        double tasa = "OPERATIVO".equalsIgnoreCase(getEstadoOperativo()) ? 0.12 : 0.25;
        return getPrecioBase() * tasa;
    }

    @Override
    public String ejecutarDiagnostico() {
        int consumoAgregado = calcularConsumoComponentesAgregados();
        int margenWatts = wattsFuente - consumoAgregado;
        String alertaFuente = (margenWatts < 150)
                ? "ALERTA: Margen de fuente ajustado (" + margenWatts + "W libres)"
                : "OK: Fuente certificada con margen seguro (" + wattsFuente + "W)";
        return String.format("Diagnóstico PC Escritorio [%s]: CPU %s | RAM %dGB | Fuente: %s | GPU Dedicada: %s",
                getCodigoSerie(), getProcesador(), getMemoriaRamGb(), alertaFuente,
                tieneGpuDedicada ? "Sí" : "No");
    }

    @Override
    public boolean requiereMantenimientoUrgente() {
        return !"OPERATIVO".equalsIgnoreCase(getEstadoOperativo()) || wattsFuente < 450;
    }

    @Override
    public String obtenerFichaTecnica() {
        return String.format(
                "ID: %d | [%s] %s %s (S/N: %s)%n" +
                "   Especificaciones: CPU %s | RAM %d GB | Almacenamiento %d GB%n" +
                "   Gabinete: %s | Fuente: %dW | GPU Dedicada: %s | Estado: %s%n" +
                "   Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones registradas: %d | Componentes agregados: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                getProcesador(), getMemoriaRamGb(), getAlmacenamientoGb(),
                factorFormaGabinete, wattsFuente, tieneGpuDedicada ? "Sí" : "No", getEstadoOperativo(),
                getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size(),
                getComponentesAgregados().size()
        );
    }

    public int getWattsFuente() { return wattsFuente; }
    public void setWattsFuente(int wattsFuente) { this.wattsFuente = wattsFuente; }

    public String getFactorFormaGabinete() { return factorFormaGabinete; }
    public void setFactorFormaGabinete(String factorFormaGabinete) { this.factorFormaGabinete = factorFormaGabinete; }

    public boolean isTieneGpuDedicada() { return tieneGpuDedicada; }
    public void setTieneGpuDedicada(boolean tieneGpuDedicada) { this.tieneGpuDedicada = tieneGpuDedicada; }
}
