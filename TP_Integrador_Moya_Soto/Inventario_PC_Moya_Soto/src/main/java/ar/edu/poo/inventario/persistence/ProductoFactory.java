package ar.edu.poo.inventario.persistence;

import ar.edu.poo.inventario.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Patron de Diseno FACTORY METHOD para reconstruir polimorficamente la subclase concreta
 * de ProductoInventario a partir de un registro proveniente de SQLite.
 */
public class ProductoFactory {

    private ProductoFactory() {}

    public static ProductoInventario crearDesdeResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo_categoria");
        String codigo = rs.getString("codigo_serie");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        double precio = rs.getDouble("precio_base");
        String estado = rs.getString("estado_operativo");

        switch (tipo) {
            case "PC_ESCRITORIO":
                return new PcEscritorio(
                        id, codigo, marca, modelo, precio, estado,
                        rs.getString("procesador"),
                        rs.getInt("memoria_ram_gb"),
                        rs.getInt("almacenamiento_gb"),
                        rs.getInt("watts_fuente"),
                        rs.getString("factor_forma"),
                        rs.getInt("tiene_gpu_dedicada") == 1
                );
            case "NOTEBOOK":
                return new Notebook(
                        id, codigo, marca, modelo, precio, estado,
                        rs.getString("procesador"),
                        rs.getInt("memoria_ram_gb"),
                        rs.getInt("almacenamiento_gb"),
                        rs.getDouble("pulgadas_pantalla"),
                        rs.getInt("capacidad_bateria_wh"),
                        rs.getInt("ciclos_carga")
                );
            case "COMP_CPU":
                return new Microprocesador(
                        id, codigo, marca, modelo, precio, estado,
                        rs.getString("tipo_interfaz"),
                        rs.getInt("consumo_watts"),
                        rs.getInt("garantia_meses"),
                        rs.getInt("cantidad_nucleos"),
                        rs.getDouble("frecuencia_ghz"),
                        rs.getString("socket")
                );
            case "COMP_RAM":
                return new ModuloMemoriaRam(
                        id, codigo, marca, modelo, precio, estado,
                        rs.getString("tipo_interfaz"),
                        rs.getInt("consumo_watts"),
                        rs.getInt("garantia_meses"),
                        rs.getInt("capacidad_comp_gb"),
                        rs.getString("tecnologia_mem_disco"),
                        rs.getInt("frecuencia_mhz")
                );
            case "COMP_DISCO":
                return new DiscoAlmacenamiento(
                        id, codigo, marca, modelo, precio, estado,
                        rs.getString("tipo_interfaz"),
                        rs.getInt("consumo_watts"),
                        rs.getInt("garantia_meses"),
                        rs.getInt("capacidad_comp_gb"),
                        rs.getString("tecnologia_mem_disco"),
                        rs.getInt("salud_smart")
                );
            default:
                throw new SQLException("Categoria de producto desconocida en la base de datos: " + tipo);
        }
    }
}
