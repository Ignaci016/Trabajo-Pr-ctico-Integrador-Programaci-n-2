package ar.edu.poo.inventario.model;

import java.util.Locale;

/**
 * Clase Concreta (Nivel 2 de herencia): Modulo de Memoria RAM.
 */
public class ModuloMemoriaRam extends ComponenteHardware {
    private int capacidadGb;
    private String tecnologiaDdr; // Se admiten DDR1 a DDR5
    private int frecuenciaMhz;

    public ModuloMemoriaRam(int id, String codigoSerie, String marca, String modelo,
                            double precioBase, String estadoOperativo,
                            String tipoInterfaz, int consumoWatts, int garantiaMeses,
                            int capacidadGb, String tecnologiaDdr, int frecuenciaMhz) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              tipoInterfaz, consumoWatts, garantiaMeses);
        this.capacidadGb = capacidadGb;
        this.tecnologiaDdr = normalizarTecnologiaDdr(tecnologiaDdr);
        validarFrecuenciaParaTecnologia(this.tecnologiaDdr, frecuenciaMhz);
        this.frecuenciaMhz = frecuenciaMhz;
    }

    @Override
    public String getTipoCategoria() {
        return "COMP_RAM";
    }

    @Override
    public double calcularDepreciacion() {
        // Se conserva el criterio original: DDR5 5%, DDR4 10% y DDR1-DDR3 20%.
        if ("DDR5".equalsIgnoreCase(tecnologiaDdr)) {
            return getPrecioBase() * 0.05;
        } else if ("DDR4".equalsIgnoreCase(tecnologiaDdr)) {
            return getPrecioBase() * 0.10;
        } else {
            return getPrecioBase() * 0.20;
        }
    }

    @Override
    public String ejecutarDiagnostico() {
        // La frecuencia ya fue validada al crear o modificar el modulo.
        // Este metodo informa la compatibilidad; no ejecuta pruebas fisicas como MemTest86.
        String estadoFrecuencia = "Frecuencia dentro del rango admitido para " + tecnologiaDdr;
        return String.format("Diagnostico RAM [%s]: %d GB %s @ %d MHz | %s",
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
                "   Memoria: %d GB %s @ %d MHz | Interfaz: %s | Consumo: %dW | Garantia: %d meses%n" +
                "   Estado: %s | Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                capacidadGb, tecnologiaDdr, frecuenciaMhz, getTipoInterfaz(), getConsumoWatts(), getGarantiaMeses(),
                getEstadoOperativo(), getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size()
        );
    }

    public int getCapacidadGb() { return capacidadGb; }
    public void setCapacidadGb(int capacidadGb) { this.capacidadGb = capacidadGb; }

    public String getTecnologiaDdr() { return tecnologiaDdr; }

    /** Normaliza y valida las tecnologias DDR admitidas por el sistema. */
    public static String normalizarTecnologiaDdr(String tecnologiaDdr) {
        if (tecnologiaDdr == null) {
            throw new IllegalArgumentException(
                    "La tecnologia DDR no puede estar vacia. Solo se permite DDR1, DDR2, DDR3, DDR4 o DDR5.");
        }

        String tecnologiaNormalizada = tecnologiaDdr.trim().toUpperCase(Locale.ROOT);
        return switch (tecnologiaNormalizada) {
            case "DDR1", "DDR2", "DDR3", "DDR4", "DDR5" -> tecnologiaNormalizada;
            default -> throw new IllegalArgumentException(
                    "Tecnologia DDR no valida: '" + tecnologiaDdr
                    + "'. Solo se permite DDR1, DDR2, DDR3, DDR4 o DDR5.");
        };
    }

    /**
     * Valida primero el rango general (DDR1 a DDR5) y luego el rango
     * correspondiente a la tecnologia elegida.
     * Los valores se expresan con la nomenclatura MHz utilizada por el sistema
     * para las velocidades comerciales de memoria.
     */
    public static void validarFrecuenciaParaTecnologia(String tecnologiaDdr, int frecuenciaMhz) {
        String tecnologia = normalizarTecnologiaDdr(tecnologiaDdr);

        if (frecuenciaMhz < 200 || frecuenciaMhz > 8400) {
            throw new IllegalArgumentException(
                    "Frecuencia fuera del rango general admitido (200 a 8400 MHz, desde DDR1 hasta DDR5).");
        }

        int minimo;
        int maximo;
        switch (tecnologia) {
            case "DDR1" -> { minimo = 200; maximo = 400; }
            case "DDR2" -> { minimo = 400; maximo = 1066; }
            case "DDR3" -> { minimo = 800; maximo = 2133; }
            case "DDR4" -> { minimo = 1600; maximo = 3200; }
            case "DDR5" -> { minimo = 3200; maximo = 8400; }
            default -> throw new IllegalArgumentException("Tecnologia DDR no admitida.");
        }

        if (frecuenciaMhz < minimo || frecuenciaMhz > maximo) {
            throw new IllegalArgumentException(
                    String.format("La frecuencia de %d MHz no corresponde a %s. "
                            + "El rango admitido para %s es de %d a %d MHz.",
                            frecuenciaMhz, tecnologia, tecnologia, minimo, maximo));
        }
    }

    public void setTecnologiaDdr(String tecnologiaDdr) {
        String tecnologiaValidada = normalizarTecnologiaDdr(tecnologiaDdr);
        validarFrecuenciaParaTecnologia(tecnologiaValidada, frecuenciaMhz);
        this.tecnologiaDdr = tecnologiaValidada;
    }

    public int getFrecuenciaMhz() { return frecuenciaMhz; }
    public void setFrecuenciaMhz(int frecuenciaMhz) {
        validarFrecuenciaParaTecnologia(tecnologiaDdr, frecuenciaMhz);
        this.frecuenciaMhz = frecuenciaMhz;
    }
}
