package ar.edu.poo.inventario.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase Abstracta Intermedia (Nivel 1 de herencia): representa una computadora completa.
 * Implementa una relación de AGREGACIÓN con ComponenteHardware: los componentes pueden
 * instalarse o retirarse de la PC y siguen existiendo de forma independiente en el inventario.
 */
public abstract class EquipoComputo extends ProductoInventario {
    private String procesador;
    private int memoriaRamGb;
    private int almacenamientoGb;

    // AGREGACIÓN: Lista de componentes independientes asociados a este equipo
    private final List<ComponenteHardware> componentesAgregados;

    protected EquipoComputo(int id, String codigoSerie, String marca, String modelo,
                            double precioBase, String estadoOperativo,
                            String procesador, int memoriaRamGb, int almacenamientoGb) {
        super(id, codigoSerie, marca, modelo, precioBase, estadoOperativo);
        this.procesador = procesador;
        this.memoriaRamGb = memoriaRamGb;
        this.almacenamientoGb = almacenamientoGb;
        this.componentesAgregados = new ArrayList<>();
    }

    public void agregarComponente(ComponenteHardware componente) {
        if (componente != null) {
            this.componentesAgregados.add(componente);
        }
    }

    public boolean desvincularComponentePorCodigo(String codigoSerieComponente) {
        Iterator<ComponenteHardware> it = componentesAgregados.iterator();
        while (it.hasNext()) {
            ComponenteHardware c = it.next();
            if (c.getCodigoSerie().equalsIgnoreCase(codigoSerieComponente)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int calcularConsumoComponentesAgregados() {
        int totalWatts = 0;
        for (ComponenteHardware c : componentesAgregados) {
            totalWatts += c.getConsumoWatts();
        }
        return totalWatts;
    }

    public String getProcesador() { return procesador; }
    public void setProcesador(String procesador) { this.procesador = procesador; }

    public int getMemoriaRamGb() { return memoriaRamGb; }
    public void setMemoriaRamGb(int memoriaRamGb) { this.memoriaRamGb = memoriaRamGb; }

    public int getAlmacenamientoGb() { return almacenamientoGb; }
    public void setAlmacenamientoGb(int almacenamientoGb) { this.almacenamientoGb = almacenamientoGb; }

    public List<ComponenteHardware> getComponentesAgregados() {
        return componentesAgregados;
    }
}
