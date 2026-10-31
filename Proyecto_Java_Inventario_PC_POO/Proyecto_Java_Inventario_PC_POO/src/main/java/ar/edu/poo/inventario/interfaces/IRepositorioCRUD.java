package ar.edu.poo.inventario.interfaces;

import ar.edu.poo.inventario.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD de persistencia (Patrón DAO + Principio DIP de SOLID).
 * @param <T> Tipo de entidad a persistir.
 */
public interface IRepositorioCRUD<T> {
    int crear(T entidad) throws PersistenciaException;
    T obtenerPorId(int id) throws PersistenciaException;
    List<T> listarTodos() throws PersistenciaException;
    boolean actualizar(T entidad) throws PersistenciaException;
    boolean eliminar(int id) throws PersistenciaException;
}
