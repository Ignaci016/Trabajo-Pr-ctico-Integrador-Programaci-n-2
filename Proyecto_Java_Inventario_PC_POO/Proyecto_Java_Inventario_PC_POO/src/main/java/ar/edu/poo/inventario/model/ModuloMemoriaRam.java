package ar.edu.poo.inventario.model;

/**
 * Clase Concreta (Nivel 2 de herencia): Módulo de Memoria RAM.
 */
public class ModuloMemoriaRam extends ComponenteHardware {
    private int capacidadGb;
    private String tecnologiaDdr; // DDR4, DDR5
    private int frecuenciaMhz;

    public ModuloMemoriaRam(int id, String codigoSerie, String marca, String modelo,
                            double precioBase, String estadoOperativo,
                            String tipoInterfaz, int consumoWatts, int garantiaMeses,
                            int capacidadGb, String tecnologiaDdr, int frecuenciaMhz) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              tipoInterfaz, consumoWatts, garantiaMeses);
        this.capacidadGb = capacidadGb;
        this.tecnologiaDdr = tecnologiaDdr;
        this.frecuenciaMhz = frecuenciaMhz;
    }

    @Override
    public String getTipoCategoria() {
        return "COMP_RAM";
    }

    @Override
    public double calcularDepreciacion() {
        // La generación de memoria (DDR3, DDR4, DDR5) cambia la tasa de depreciación por obsolescencia
        if ("DDR5".equalsIgnoreCase(tecnologiaDdr)) {
            return getPrecioBase() * 0.05; // 5% para tecnología actual DDR5
        } else if ("DDR4".equalsIgnoreCase(tecnologiaDdr)) {
            return getPrecioBase() * 0.10; // 10% para estándar DDR4
        } else {
            return getPrecioBase() * 0.20; // 20% para DDR3 o anteriores
        }
    }

    @Override
    public String ejecutarDiagnostico() {
        String estadoFrecuencia;
        if ("DDR5".equalsIgnoreCase(tecnologiaDdr) && frecuenciaMhz < 4800) {
            estadoFrecuencia = "ADVERTENCIA: Frecuencia por debajo del estándar JEDEC DDR5 (4800 MHz)";
        } else if ("DDR4".equalsIgnoreCase(tecnologiaDdr) && frecuenciaMhz < 2133) {
            estadoFrecuencia = "ADVERTENCIA: Frecuencia por debajo del estándar JEDEC DDR4 (2133 MHz)";
        } else {
            estadoFrecuencia = "Verificación MemTest86 sin errores de paridad (Frecuencia acorde a " + tecnologiaDdr + ")";
        }
        return String.format("Diagnóstico RAM [%s]: %d GB %s @ %d MHz | %s",
                getCodigoSerie(), capacidadGb, tecnologiaDdr, frecuenciaMhz, estadoFrecuencia);
    }

    @Override
    public boolean requiereMantenimientoUrgente() {
        return !"OPERATIVO".equalsIgnoreCase(getEstadoOperativo());
    }

    @Override
    public String obtenerFichaTecnica() {
        return String.format(
                "ID: %d | [%s] %s %s (S/N: %s)%n" +
                "   Memoria: %d GB %s @ %d MHz | Interfaz: %s | Consumo: %dW | Garantía: %d meses%n" +
                "   Estado: %s | Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                capacidadGb, tecnologiaDdr, frecuenciaMhz, getTipoInterfaz(), getConsumoWatts(), getGarantiaMeses(),
                getEstadoOperativo(), getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size()
        );
    }

    public int getCapacidadGb() { return capacidadGb; }
    public void setCapacidadGb(int capacidadGb) { this.capacidadGb = capacidadGb; }

    public String getTecnologiaDdr() { return tecnologiaDdr; }
    public void setTecnologiaDdr(String tecnologiaDdr) { this.tecnologiaDdr = tecnologiaDdr; }

    public int getFrecuenciaMhz() { return frecuenciaMhz; }
    public void setFrecuenciaMhz(int frecuenciaMhz) { this.frecuenciaMhz = frecuenciaMhz; }
}
