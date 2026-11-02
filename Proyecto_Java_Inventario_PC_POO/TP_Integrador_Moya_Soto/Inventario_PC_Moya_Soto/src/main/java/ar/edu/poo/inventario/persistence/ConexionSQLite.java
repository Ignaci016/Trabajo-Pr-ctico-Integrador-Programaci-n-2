package ar.edu.poo.inventario.persistence;

import ar.edu.poo.inventario.exceptions.PersistenciaException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestiona la conexion a la base de datos SQLite aplicando el Patron de Diseno SINGLETON.
 * Garantiza una unica instancia activa y crea el esquema relacional si no existe.
 */
public class ConexionSQLite {
    private static ConexionSQLite instancia;
    private Connection conexion;
    private static final String URL_DB = "jdbc:sqlite:inventario_tech.db";

    private ConexionSQLite() throws PersistenciaException {
        try {
            this.conexion = DriverManager.getConnection(URL_DB);
            inicializarEsquema();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar a la base de datos SQLite: " + e.getMessage(), e);
        }
    }

    public static synchronized ConexionSQLite getInstancia() throws PersistenciaException {
        if (instancia == null || instancia.conexion == null) {
            instancia = new ConexionSQLite();
        } else {
            try {
                if (instancia.conexion.isClosed()) {
                    instancia = new ConexionSQLite();
                }
            } catch (SQLException e) {
                instancia = new ConexionSQLite();
            }
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    private void inicializarEsquema() throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            // Activar soporte de claves foraneas en SQLite (ON DELETE CASCADE para Composicion)
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Tabla principal polimorfica de productos (PCs y Componentes)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS productos (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  tipo_categoria TEXT NOT NULL," +
                "  codigo_serie TEXT NOT NULL UNIQUE," +
                "  marca TEXT NOT NULL," +
                "  modelo TEXT NOT NULL," +
                "  precio_base REAL NOT NULL," +
                "  estado_operativo TEXT NOT NULL," +
                "  procesador TEXT," +
                "  memoria_ram_gb INTEGER DEFAULT 0," +
                "  almacenamiento_gb INTEGER DEFAULT 0," +
                "  watts_fuente INTEGER DEFAULT 0," +
                "  factor_forma TEXT," +
                "  tiene_gpu_dedicada INTEGER DEFAULT 0," +
                "  pulgadas_pantalla REAL DEFAULT 0.0," +
                "  capacidad_bateria_wh INTEGER DEFAULT 0," +
                "  ciclos_carga INTEGER DEFAULT 0," +
                "  tipo_interfaz TEXT," +
                "  consumo_watts INTEGER DEFAULT 0," +
                "  garantia_meses INTEGER DEFAULT 0," +
                "  cantidad_nucleos INTEGER DEFAULT 0," +
                "  frecuencia_ghz REAL DEFAULT 0.0," +
                "  socket TEXT," +
                "  capacidad_comp_gb INTEGER DEFAULT 0," +
                "  tecnologia_mem_disco TEXT," +
                "  frecuencia_mhz INTEGER DEFAULT 0," +
                "  salud_smart INTEGER DEFAULT 100" +
                ");"
            );

            // Tabla de revisiones tecnicas (COMPOSICION: ON DELETE CASCADE)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS revisiones_tecnicas (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  producto_id INTEGER NOT NULL," +
                "  fecha TEXT NOT NULL," +
                "  tecnico_responsable TEXT NOT NULL," +
                "  aclaracion_detallada TEXT NOT NULL," +
                "  aprobado INTEGER NOT NULL," +
                "  costo_servicio REAL NOT NULL DEFAULT 0.0," +
                "  FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE" +
                ");"
            );

            // Tabla de agregacion Equipo <-> Componente (AGREGACION: ON DELETE SET NULL / CASCADE en vinculo)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS equipo_componentes (" +
                "  equipo_id INTEGER NOT NULL," +
                "  componente_id INTEGER NOT NULL," +
                "  PRIMARY KEY (equipo_id, componente_id)," +
                "  FOREIGN KEY (equipo_id) REFERENCES productos(id) ON DELETE CASCADE," +
                "  FOREIGN KEY (componente_id) REFERENCES productos(id) ON DELETE CASCADE" +
                ");"
            );
        }
    }
}
