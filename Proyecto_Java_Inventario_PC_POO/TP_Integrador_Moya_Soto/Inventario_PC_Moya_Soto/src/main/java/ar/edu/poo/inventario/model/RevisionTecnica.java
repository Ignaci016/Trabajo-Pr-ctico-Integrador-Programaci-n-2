package ar.edu.poo.inventario.model;

import java.time.LocalDate;

/**
 * Representa una revision o auditoria tecnica realizada a una PC o componente.
 * Relacion de COMPOSICION con ProductoInventario: una revision pertenece exclusivamente
 * a un producto y no tiene existencia independiente fuera de el.
 */
public class RevisionTecnica {
    private int id;
    private int productoId;
    private String fecha;
    private String tecnicoResponsable;
    private String aclaracionDetallada;
    private boolean aprobado;
    private double costoServicio;

    public RevisionTecnica(int id, int productoId, String fecha, String tecnicoResponsable,
                           String aclaracionDetallada, boolean aprobado, double costoServicio) {
        this.id = id;
        this.productoId = productoId;
        this.fecha = (fecha == null || fecha.isBlank()) ? LocalDate.now().toString() : fecha;
        this.tecnicoResponsable = tecnicoResponsable;
        this.aclaracionDetallada = aclaracionDetallada;
        this.aprobado = aprobado;
        this.costoServicio = Math.max(0.0, costoServicio);
    }

    public RevisionTecnica(String tecnicoResponsable, String aclaracionDetallada, boolean aprobado, double costoServicio) {
        this(0, 0, LocalDate.now().toString(), tecnicoResponsable, aclaracionDetallada, aprobado, costoServicio);
    }

    // Getters y Setters encapsulados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTecnicoResponsable() { return tecnicoResponsable; }
    public void setTecnicoResponsable(String tecnicoResponsable) { this.tecnicoResponsable = tecnicoResponsable; }

    public String getAclaracionDetallada() { return aclaracionDetallada; }
    public void setAclaracionDetallada(String aclaracionDetallada) { this.aclaracionDetallada = aclaracionDetallada; }

    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    public double getCostoServicio() { return costoServicio; }
    public void setCostoServicio(double costoServicio) { this.costoServicio = Math.max(0.0, costoServicio); }

    @Override
    public String toString() {
        String resultado = aprobado ? "APROBADA" : "CON OBSERVACIONES / RECHAZADA";
        return String.format("[%s] Rev #%d | Tecnico: %s | Estado: %s | Costo: $%.2f%n      -> Aclaracion: %s",
                fecha, id, tecnicoResponsable, resultado, costoServicio, aclaracionDetallada);
    }
}
