package ar.edu.poo.inventario.model;

/**
 * Clase Concreta (Nivel 2 de herencia): Notebook / Computadora Portatil.
 */
public class Notebook extends EquipoComputo {
    private double pulgadasPantalla;
    private int capacidadBateriaWh;
    private int ciclosCarga;

    public Notebook(int id, String codigoSerie, String marca, String modelo,
                    double precioBase, String estadoOperativo,
                    String procesador, int memoriaRamGb, int almacenamientoGb,
                    double pulgadasPantalla, int capacidadBateriaWh, int ciclosCarga) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo,
              procesador, memoriaRamGb, almacenamientoGb);
        this.pulgadasPantalla = pulgadasPantalla;
        this.capacidadBateriaWh = capacidadBateriaWh;
        this.ciclosCarga = ciclosCarga;
    }

    @Override
    public String getTipoCategoria() {
        return "NOTEBOOK";
    }

    @Override
    public double calcularDepreciacion() {
        // Depreciacion segun desgaste de bateria (ciclos de carga) y estado operativo
        double desgasteBateria = Math.min(0.35, (ciclosCarga / 1000.0) * 0.25);
        return getPrecioBase() * (0.15 + desgasteBateria);
    }

    @Override
    public String ejecutarDiagnostico() {
        String saludBateria = (ciclosCarga > 500)
                ? "Reemplazo de bateria recomendado (" + ciclosCarga + " ciclos)"
                : "Bateria saludable (" + ciclosCarga + " ciclos, " + capacidadBateriaWh + "Wh)";
        return String.format("Diagnostico Notebook [%s]: Pantalla %.1f pulg. | CPU %s | %s",
                getCodigoSerie(), pulgadasPantalla, getProcesador(), saludBateria);
    }

    @Override
    public boolean requiereMantenimientoUrgente() {
        return !"OPERATIVO".equalsIgnoreCase(getEstadoOperativo()) || ciclosCarga > 600;
    }

    @Override
    public String obtenerFichaTecnica() {
        return String.format(
                "ID: %d | [%s] %s %s (S/N: %s)%n" +
                "   Especificaciones: CPU %s | RAM %d GB | SSD %d GB | Pantalla %.1f pulg.%n" +
                "   Bateria: %d Wh (%d ciclos de carga) | Estado: %s%n" +
                "   Precio Base: $%.2f | Valor Actual: $%.2f | Revisiones registradas: %d",
                getId(), getTipoCategoria(), getMarca(), getModelo(), getCodigoSerie(),
                getProcesador(), getMemoriaRamGb(), getAlmacenamientoGb(), pulgadasPantalla,
                capacidadBateriaWh, ciclosCarga, getEstadoOperativo(),
                getPrecioBase(), calcularValorActual(), getHistorialRevisiones().size()
        );
    }

    public double getPulgadasPantalla() { return pulgadasPantalla; }
    public void setPulgadasPantalla(double pulgadasPantalla) { this.pulgadasPantalla = pulgadasPantalla; }

    public int getCapacidadBateriaWh() { return capacidadBateriaWh; }
    public void setCapacidadBateriaWh(int capacidadBateriaWh) { this.capacidadBateriaWh = capacidadBateriaWh; }

    public int getCiclosCarga() { return ciclosCarga; }
    public void setCiclosCarga(int ciclosCarga) { this.ciclosCarga = ciclosCarga; }
}
