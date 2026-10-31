package ar.edu.poo.inventario.model;

import ar.edu.poo.inventario.exceptions.PosicionListaInvalidaException;
import ar.edu.poo.inventario.interfaces.Diagnosticable;
import ar.edu.poo.inventario.interfaces.Inventariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase Abstracta Raíz (Nivel 0 de la jerarquía de herencia).
 * Implementa las interfaces Inventariable y Diagnosticable.
 * Demuestra encapsulamiento, composición (RevisionTecnica), sobrecarga de métodos e iteradores.
 */
public abstract class ProductoInventario implements Inventariable, Diagnosticable {
    private int id;
    private String codigoSerie;
    private String marca;
    private String modelo;
    private double precioBase;
    private String estadoOperativo; // OPERATIVO, EN_REVISION, DEFECTUOSO

    // COMPOSICIÓN: El historial de revisiones nace y muere con el ProductoInventario
    private final List<RevisionTecnica> historialRevisiones;

    protected ProductoInventario(int id, String codigoSerie, String marca, String modelo,
                                 double precioBase, String estadoOperativo) {
        this.id = id;
        this.codigoSerie = codigoSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.precioBase = precioBase;
        this.estadoOperativo = estadoOperativo;
        this.historialRevisiones = new ArrayList<>();
    }

    // Métodos abstractos que obligan a la especialización en subclases
    public abstract String getTipoCategoria();
    public abstract double calcularDepreciacion();

    // =========================================================================
    // SOBRECARGA DE MÉTODOS (Method Overloading)
    // =========================================================================

    /**
     * Sobrecarga 1: Registra una revisión técnica rápida sin costo de repuestos ($0.0).
     */
    public RevisionTecnica registrarRevision(String tecnico, String aclaracion, boolean aprobado) {
        return registrarRevision(tecnico, aclaracion, aprobado, 0.0);
    }

    /**
     * Sobrecarga 2: Registra una revisión técnica especificando el costo del servicio/repuestos.
     */
    public RevisionTecnica registrarRevision(String tecnico, String aclaracion, boolean aprobado, double costoServicio) {
        RevisionTecnica nueva = new RevisionTecnica(tecnico, aclaracion, aprobado, costoServicio);
        nueva.setProductoId(this.id);
        this.historialRevisiones.add(nueva);
        this.estadoOperativo = aprobado ? "OPERATIVO" : "EN_REVISION";
        return nueva;
    }

    /**
     * Sobrecarga 3: Registra un objeto RevisionTecnica ya hidratado desde la base de datos.
     */
    public void registrarRevision(RevisionTecnica revision) {
        if (revision != null) {
            revision.setProductoId(this.id);
            this.historialRevisiones.add(revision);
        }
    }

    /**
     * Implementación base de Inventariable: calcula valor actual restando la depreciación polimórfica.
     */
    @Override
    public double calcularValorActual() {
        double depreciacion = calcularDepreciacion();
        return Math.max(0.0, precioBase - depreciacion);
    }

    /**
     * Sobrecarga del cálculo de valor actual aplicando un porcentaje de descuento adicional.
     */
    public double calcularValorActual(double porcentajeDescuentoExtra) {
        double base = calcularValorActual();
        double factor = Math.max(0.0, Math.min(100.0, porcentajeDescuentoExtra)) / 100.0;
        return base * (1.0 - factor);
    }

    // =========================================================================
    // ACCESO VALIDADO A LA LISTA Y RECORRIDO CON ITERATOR
    // =========================================================================

    public RevisionTecnica obtenerRevisionEnPosicion(int indice) throws PosicionListaInvalidaException {
        if (indice < 0 || indice >= historialRevisiones.size()) {
            throw new PosicionListaInvalidaException(indice, historialRevisiones.size());
        }
        return historialRevisiones.get(indice);
    }

    /**
     * Elimina una revisión por su ID utilizando explícitamente java.util.Iterator
     * para evitar ConcurrentModificationException.
     */
    public boolean eliminarRevisionPorIdConIterator(int revisionId) {
        Iterator<RevisionTecnica> it = historialRevisiones.iterator();
        while (it.hasNext()) {
            RevisionTecnica rev = it.next();
            if (rev.getId() == revisionId) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public String obtenerResumenEstado() {
        return String.format("[%s] %s %s (S/N: %s) | Estado: %s | Revisiones: %d | Valor Actual: $%.2f",
                getTipoCategoria(), marca, modelo, codigoSerie, estadoOperativo,
                historialRevisiones.size(), calcularValorActual());
    }

    // Getters y Setters con encapsulamiento
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoSerie() { return codigoSerie; }
    public void setCodigoSerie(String codigoSerie) { this.codigoSerie = codigoSerie; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public List<RevisionTecnica> getHistorialRevisiones() {
        return historialRevisiones;
    }
}
