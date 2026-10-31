package ar.edu.poo.inventario.service;

import ar.edu.poo.inventario.exceptions.EntradaInvalidaException;
import ar.edu.poo.inventario.exceptions.PersistenciaException;
import ar.edu.poo.inventario.exceptions.PosicionListaInvalidaException;
import ar.edu.poo.inventario.model.*;
import ar.edu.poo.inventario.persistence.ProductoDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Capa de Servicio que coordina la lógica de negocio, la persistencia en SQLite
 * y la manipulación de colecciones utilizando estructuras for, while e Iterator.
 */
public class InventarioService {
    private final ProductoDAO productoDAO;
    private List<ProductoInventario> cacheInventario;

    public InventarioService(ProductoDAO productoDAO) throws PersistenciaException {
        this.productoDAO = productoDAO;
        this.cacheInventario = new ArrayList<>();
        sincronizarConBaseDeDatos();
    }

    public final void sincronizarConBaseDeDatos() throws PersistenciaException {
        this.cacheInventario = productoDAO.listarTodos();
    }

    public int registrarProducto(ProductoInventario nuevo) throws EntradaInvalidaException, PersistenciaException {
        if (nuevo == null) {
            throw new EntradaInvalidaException("Producto", "El objeto producto no puede ser nulo.");
        }
        if (nuevo.getPrecioBase() <= 0) {
            throw new EntradaInvalidaException("precioBase", "El precio base debe ser mayor a $0.00.");
        }
        int id = productoDAO.crear(nuevo);
        sincronizarConBaseDeDatos();
        return id;
    }

    public void registrarRevisionEnProducto(int indiceLista, String tecnico, String aclaracion,
                                            boolean aprobado, double costo)
            throws PosicionListaInvalidaException, EntradaInvalidaException, PersistenciaException {
        ProductoInventario prod = obtenerPorPosicionEnLista(indiceLista);
        if (aclaracion == null || aclaracion.trim().length() < 5) {
            throw new EntradaInvalidaException("aclaracionDetallada",
                    "Debe especificar una aclaración técnica detallada (mínimo 5 caracteres).");
        }
        RevisionTecnica rev = prod.registrarRevision(tecnico, aclaracion, aprobado, costo);
        productoDAO.insertarRevision(rev);
        productoDAO.actualizar(prod);
        sincronizarConBaseDeDatos();
    }

    /**
     * Valida y retorna un producto según su índice en la lista en memoria.
     * Lanza PosicionListaInvalidaException si el índice está fuera de rango.
     */
    public ProductoInventario obtenerPorPosicionEnLista(int indice) throws PosicionListaInvalidaException {
        if (indice < 0 || indice >= cacheInventario.size()) {
            throw new PosicionListaInvalidaException(indice, cacheInventario.size());
        }
        return cacheInventario.get(indice);
    }

    /**
     * Demuestra el recorrido con estructura FOR clásica para listar productos con su índice.
     */
    public List<String> generarListadoNumeradoConFor() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < cacheInventario.size(); i++) {
            ProductoInventario p = cacheInventario.get(i);
            lineas.add(String.format("Posición [%d] -> ID BD #%d | %s", i, p.getId(), p.obtenerResumenEstado()));
        }
        return lineas;
    }

    /**
     * Demuestra el recorrido con estructura WHILE para calcular indicadores económicos del inventario.
     */
    public double calcularValorTotalInventarioConWhile() {
        double sumaTotal = 0.0;
        int idx = 0;
        while (idx < cacheInventario.size()) {
            sumaTotal += cacheInventario.get(idx).calcularValorActual();
            idx++;
        }
        return sumaTotal;
    }

    /**
     * Demuestra el uso explícito de java.util.Iterator para recorrer y filtrar/manipular
     * colecciones (obteniendo todos los productos que requieren mantenimiento urgente).
     */
    public List<ProductoInventario> filtrarMantenimientoUrgenteConIterator() {
        List<ProductoInventario> copia = new ArrayList<>(cacheInventario);
        Iterator<ProductoInventario> iterator = copia.iterator();
        while (iterator.hasNext()) {
            ProductoInventario item = iterator.next();
            if (!item.requiereMantenimientoUrgente()) {
                iterator.remove(); // Manipulación segura de la lista mediante Iterator
            }
        }
        return copia;
    }

    public boolean actualizarPrecioYEstado(int indiceLista, double nuevoPrecio, String nuevoEstado)
            throws PosicionListaInvalidaException, EntradaInvalidaException, PersistenciaException {
        ProductoInventario prod = obtenerPorPosicionEnLista(indiceLista);
        if (nuevoPrecio <= 0) {
            throw new EntradaInvalidaException("precioBase", "El nuevo precio debe ser positivo.");
        }
        prod.setPrecioBase(nuevoPrecio);
        prod.setEstadoOperativo(nuevoEstado);
        boolean ok = productoDAO.actualizar(prod);
        sincronizarConBaseDeDatos();
        return ok;
    }

    public boolean eliminarProductoPorPosicion(int indiceLista)
            throws PosicionListaInvalidaException, PersistenciaException {
        ProductoInventario prod = obtenerPorPosicionEnLista(indiceLista);
        boolean ok = productoDAO.eliminar(prod.getId());
        sincronizarConBaseDeDatos();
        return ok;
    }

    public List<ProductoInventario> buscarPorCriterioSQL(String texto) throws PersistenciaException {
        return productoDAO.buscarPorTextoSQL(texto);
    }

    public List<ProductoInventario> filtrarPorEstadoSQL(String estado) throws PersistenciaException {
        return productoDAO.filtrarPorEstadoSQL(estado);
    }

    public void asociarComponenteAPC(int equipoId, int componenteId) throws PersistenciaException {
        productoDAO.vincularComponenteAEquipo(equipoId, componenteId);
        sincronizarConBaseDeDatos();
    }

    public List<ProductoInventario> getCacheInventario() {
        return cacheInventario;
    }
}
