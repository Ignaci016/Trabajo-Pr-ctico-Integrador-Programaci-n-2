package ar.edu.poo.inventario.persistence;

import ar.edu.poo.inventario.exceptions.PersistenciaException;
import ar.edu.poo.inventario.interfaces.IRepositorioCRUD;
import ar.edu.poo.inventario.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del Patrón DAO (Data Access Object) para gestionar la persistencia CRUD
 * y las consultas SQL parametrizadas de productos, componentes y sus revisiones técnicas.
 */
public class ProductoDAO implements IRepositorioCRUD<ProductoInventario> {

    @Override
    public int crear(ProductoInventario p) throws PersistenciaException {
        String sql = "INSERT INTO productos (" +
                "tipo_categoria, codigo_serie, marca, modelo, precio_base, estado_operativo, " +
                "procesador, memoria_ram_gb, almacenamiento_gb, watts_fuente, factor_forma, tiene_gpu_dedicada, " +
                "pulgadas_pantalla, capacidad_bateria_wh, ciclos_carga, tipo_interfaz, consumo_watts, garantia_meses, " +
                "cantidad_nucleos, frecuencia_ghz, socket, capacidad_comp_gb, tecnologia_mem_disco, frecuencia_mhz, salud_smart" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            configurarParametrosProducto(ps, p);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int nuevoId = keys.getInt(1);
                    p.setId(nuevoId);
                    // Persistir revisiones iniciales en composición si existen
                    for (RevisionTecnica rev : p.getHistorialRevisiones()) {
                        rev.setProductoId(nuevoId);
                        insertarRevision(rev);
                    }
                    return nuevoId;
                }
            }
            throw new PersistenciaException("No se pudo obtener el ID autogenerado del producto.");
        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar producto en SQLite: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductoInventario obtenerPorId(int id) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductoInventario p = ProductoFactory.crearDesdeResultSet(rs);
                    cargarRevisionesDeProducto(p);
                    if (p instanceof EquipoComputo) {
                        cargarComponentesAgregados((EquipoComputo) p);
                    }
                    return p;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar producto por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductoInventario> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM productos ORDER BY id ASC";
        List<ProductoInventario> lista = new ArrayList<>();
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            // Uso de ciclo while para recorrer los registros de la tabla SQLite
            while (rs.next()) {
                ProductoInventario p = ProductoFactory.crearDesdeResultSet(rs);
                cargarRevisionesDeProducto(p);
                if (p instanceof EquipoComputo) {
                    cargarComponentesAgregados((EquipoComputo) p);
                }
                lista.add(p);
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar el inventario desde SQLite: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(ProductoInventario p) throws PersistenciaException {
        String sql = "UPDATE productos SET " +
                "tipo_categoria=?, codigo_serie=?, marca=?, modelo=?, precio_base=?, estado_operativo=?, " +
                "procesador=?, memoria_ram_gb=?, almacenamiento_gb=?, watts_fuente=?, factor_forma=?, tiene_gpu_dedicada=?, " +
                "pulgadas_pantalla=?, capacidad_bateria_wh=?, ciclos_carga=?, tipo_interfaz=?, consumo_watts=?, garantia_meses=?, " +
                "cantidad_nucleos=?, frecuencia_ghz=?, socket=?, capacidad_comp_gb=?, tecnologia_mem_disco=?, frecuencia_mhz=?, salud_smart=? " +
                "WHERE id=?";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            configurarParametrosProducto(ps, p);
            ps.setInt(26, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el producto ID " + p.getId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) throws PersistenciaException {
        String sql = "DELETE FROM productos WHERE id = ?";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el producto ID " + id + ": " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // CONSULTAS SQL DE BÚSQUEDA, FILTRADO Y GESTIÓN DE REVISIONES TÉCNICAS
    // =========================================================================

    public List<ProductoInventario> buscarPorTextoSQL(String termino) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE UPPER(codigo_serie) LIKE ? OR UPPER(marca) LIKE ? OR UPPER(modelo) LIKE ? ORDER BY id ASC";
        List<ProductoInventario> resultados = new ArrayList<>();
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String patron = "%" + termino.toUpperCase().trim() + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductoInventario p = ProductoFactory.crearDesdeResultSet(rs);
                    cargarRevisionesDeProducto(p);
                    resultados.add(p);
                }
            }
            return resultados;
        } catch (SQLException e) {
            throw new PersistenciaException("Error en búsqueda SQL por texto: " + e.getMessage(), e);
        }
    }

    public List<ProductoInventario> filtrarPorEstadoSQL(String estadoOperativo) throws PersistenciaException {
        String sql = "SELECT * FROM productos WHERE UPPER(estado_operativo) = ? ORDER BY id ASC";
        List<ProductoInventario> resultados = new ArrayList<>();
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estadoOperativo.toUpperCase().trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductoInventario p = ProductoFactory.crearDesdeResultSet(rs);
                    cargarRevisionesDeProducto(p);
                    resultados.add(p);
                }
            }
            return resultados;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al filtrar por estado en SQLite: " + e.getMessage(), e);
        }
    }

    public int insertarRevision(RevisionTecnica rev) throws PersistenciaException {
        String sql = "INSERT INTO revisiones_tecnicas (producto_id, fecha, tecnico_responsable, aclaracion_detallada, aprobado, costo_servicio) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rev.getProductoId());
            ps.setString(2, rev.getFecha());
            ps.setString(3, rev.getTecnicoResponsable());
            ps.setString(4, rev.getAclaracionDetallada());
            ps.setInt(5, rev.isAprobado() ? 1 : 0);
            ps.setDouble(6, rev.getCostoServicio());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    rev.setId(keys.getInt(1));
                }
            }
            return rev.getId();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al registrar revisión técnica en SQLite: " + e.getMessage(), e);
        }
    }

    public boolean actualizarAclaracionRevision(int revisionId, String nuevaAclaracion, boolean aprobado) throws PersistenciaException {
        String sql = "UPDATE revisiones_tecnicas SET aclaracion_detallada = ?, aprobado = ? WHERE id = ?";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaAclaracion);
            ps.setInt(2, aprobado ? 1 : 0);
            ps.setInt(3, revisionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar aclaración de revisión: " + e.getMessage(), e);
        }
    }

    public void vincularComponenteAEquipo(int equipoId, int componenteId) throws PersistenciaException {
        String sql = "INSERT OR IGNORE INTO equipo_componentes (equipo_id, componente_id) VALUES (?, ?)";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, equipoId);
            ps.setInt(2, componenteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al vincular componente a equipo: " + e.getMessage(), e);
        }
    }

    private void cargarRevisionesDeProducto(ProductoInventario p) throws SQLException, PersistenciaException {
        String sql = "SELECT * FROM revisiones_tecnicas WHERE producto_id = ? ORDER BY id ASC";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RevisionTecnica rev = new RevisionTecnica(
                            rs.getInt("id"),
                            rs.getInt("producto_id"),
                            rs.getString("fecha"),
                            rs.getString("tecnico_responsable"),
                            rs.getString("aclaracion_detallada"),
                            rs.getInt("aprobado") == 1,
                            rs.getDouble("costo_servicio")
                    );
                    p.registrarRevision(rev);
                }
            }
        }
    }

    private void cargarComponentesAgregados(EquipoComputo equipo) throws SQLException, PersistenciaException {
        String sql = "SELECT p.* FROM productos p " +
                     "INNER JOIN equipo_componentes ec ON p.id = ec.componente_id " +
                     "WHERE ec.equipo_id = ?";
        Connection conn = ConexionSQLite.getInstancia().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, equipo.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductoInventario comp = ProductoFactory.crearDesdeResultSet(rs);
                    if (comp instanceof ComponenteHardware) {
                        equipo.agregarComponente((ComponenteHardware) comp);
                    }
                }
            }
        }
    }

    private void configurarParametrosProducto(PreparedStatement ps, ProductoInventario p) throws SQLException {
        ps.setString(1, p.getTipoCategoria());
        ps.setString(2, p.getCodigoSerie());
        ps.setString(3, p.getMarca());
        ps.setString(4, p.getModelo());
        ps.setDouble(5, p.getPrecioBase());
        ps.setString(6, p.getEstadoOperativo());

        // Valores por defecto para columnas específicas
        ps.setString(7, null);
        ps.setInt(8, 0);
        ps.setInt(9, 0);
        ps.setInt(10, 0);
        ps.setString(11, null);
        ps.setInt(12, 0);
        ps.setDouble(13, 0.0);
        ps.setInt(14, 0);
        ps.setInt(15, 0);
        ps.setString(16, null);
        ps.setInt(17, 0);
        ps.setInt(18, 0);
        ps.setInt(19, 0);
        ps.setDouble(20, 0.0);
        ps.setString(21, null);
        ps.setInt(22, 0);
        ps.setString(23, null);
        ps.setInt(24, 0);
        ps.setInt(25, 100);

        if (p instanceof EquipoComputo) {
            EquipoComputo eq = (EquipoComputo) p;
            ps.setString(7, eq.getProcesador());
            ps.setInt(8, eq.getMemoriaRamGb());
            ps.setInt(9, eq.getAlmacenamientoGb());
            if (eq instanceof PcEscritorio) {
                PcEscritorio pc = (PcEscritorio) eq;
                ps.setInt(10, pc.getWattsFuente());
                ps.setString(11, pc.getFactorFormaGabinete());
                ps.setInt(12, pc.isTieneGpuDedicada() ? 1 : 0);
            } else if (eq instanceof Notebook) {
                Notebook nb = (Notebook) eq;
                ps.setDouble(13, nb.getPulgadasPantalla());
                ps.setInt(14, nb.getCapacidadBateriaWh());
                ps.setInt(15, nb.getCiclosCarga());
            }
        } else if (p instanceof ComponenteHardware) {
            ComponenteHardware ch = (ComponenteHardware) p;
            ps.setString(16, ch.getTipoInterfaz());
            ps.setInt(17, ch.getConsumoWatts());
            ps.setInt(18, ch.getGarantiaMeses());
            if (ch instanceof Microprocesador) {
                Microprocesador cpu = (Microprocesador) ch;
                ps.setInt(19, cpu.getCantidadNucleos());
                ps.setDouble(20, cpu.getFrecuenciaTurboGhz());
                ps.setString(21, cpu.getSocket());
            } else if (ch instanceof ModuloMemoriaRam) {
                ModuloMemoriaRam ram = (ModuloMemoriaRam) ch;
                ps.setInt(22, ram.getCapacidadGb());
                ps.setString(23, ram.getTecnologiaDdr());
                ps.setInt(24, ram.getFrecuenciaMhz());
            } else if (ch instanceof DiscoAlmacenamiento) {
                DiscoAlmacenamiento disco = (DiscoAlmacenamiento) ch;
                ps.setInt(22, disco.getCapacidadGb());
                ps.setString(23, disco.getTipoTecnologia());
                ps.setInt(25, disco.getSaludSmartPorcentaje());
            }
        }
    }
}
