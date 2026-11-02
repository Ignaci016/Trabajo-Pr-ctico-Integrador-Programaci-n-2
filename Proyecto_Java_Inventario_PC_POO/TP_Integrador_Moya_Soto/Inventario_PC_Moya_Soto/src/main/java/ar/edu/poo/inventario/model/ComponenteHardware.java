package ar.edu.poo.inventario.model;

/**
 * Clase Abstracta Intermedia (Nivel 1 de herencia): representa un componente individual de hardware.
 */
public abstract class ComponenteHardware extends ProductoInventario {
    private String tipoInterfaz; // Ej: PCIe 4.0, AM5, DDR5, NVMe M.2
    private int consumoWatts;
    private int garantiaMeses;

    protected ComponenteHardware(int id, String codigoSerie, String marca, String modelo,
                                 double precioBase, String estadoOperativo,
                                 String tipoInterfaz, int consumoWatts, int garantiaMeses) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo);
        this.tipoInterfaz = tipoInterfaz;
        this.consumoWatts = consumoWatts;
        this.garantiaMeses = garantiaMeses;
    }

    public String getTipoInterfaz() { return tipoInterfaz; }
    public void setTipoInterfaz(String tipoInterfaz) { this.tipoInterfaz = tipoInterfaz; }

    public int getConsumoWatts() { return consumoWatts; }
    public void setConsumoWatts(int consumoWatts) { this.consumoWatts = consumoWatts; }

    public int getGarantiaMeses() { return garantiaMeses; }
    public void setGarantiaMeses(int garantiaMeses) { this.garantiaMeses = garantiaMeses; }
}
