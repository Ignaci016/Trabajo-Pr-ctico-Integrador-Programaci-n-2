package ar.edu.poo.inventario;

import ar.edu.poo.inventario.exceptions.PersistenciaException;
import ar.edu.poo.inventario.model.*;
import ar.edu.poo.inventario.persistence.ProductoDAO;
import ar.edu.poo.inventario.service.InventarioService;
import ar.edu.poo.inventario.ui.ConsolaUI;

/**
 * Punto de entrada de la aplicación del Trabajo Práctico Integrador de POO.
 * Inicializa la base de datos SQLite con datos de prueba si está vacía e inicia la consola interactiva.
 */
public class Main {
    public static void main(String[] args) {
        try {
            ProductoDAO dao = new ProductoDAO();
            InventarioService service = new InventarioService(dao);

            if (service.getCacheInventario().isEmpty()) {
                cargarDatosSemilla(dao, service);
            }

            ConsolaUI ui = new ConsolaUI(service);
            ui.iniciarMenu();
        } catch (PersistenciaException e) {
            System.err.println("Error crítico de persistencia al iniciar el sistema: " + e.getMessage());
        }
    }

    private static void cargarDatosSemilla(ProductoDAO dao, InventarioService service) throws PersistenciaException {
        PcEscritorio pc1 = new PcEscritorio(
                0, "PC-WS-001", "Dell", "Precision 3660 Tower", 1450000.0, "OPERATIVO",
                "Intel Core i7-13700K", 32, 1024, 750, "ATX Mid-Tower", true
        );
        pc1.registrarRevision("Ing. Marcos Quiroga",
                "Limpieza integral por ultrasonido, recambio de pasta térmica Arctic MX-6 en CPU/GPU y test OCCT durante 45 min a 68°C estables.",
                true, 28500.0);
        pc1.registrarRevision("Téc. Sofía Lucero",
                "Actualización de BIOS v2.14 y verificación de curva PWM en coolers frontales.",
                true);

        Notebook nb1 = new Notebook(
                0, "NB-DEV-002", "Lenovo", "ThinkPad T14 Gen 3", 1180000.0, "EN_REVISION",
                "AMD Ryzen 7 PRO 6850U", 16, 512, 14.0, 52, 640
        );
        nb1.registrarRevision("Téc. Sofía Lucero",
                "Batería reporta 640 ciclos de carga con caída de autonomía al 62%. Bisagra izquierda ajustada; se solicita reemplazo de batería.",
                false, 15000.0);

        Microprocesador cpu1 = new Microprocesador(
                0, "CPU-AM5-003", "AMD", "Ryzen 5 7600X", 340000.0, "OPERATIVO",
                "Socket AM5", 105, 36, 6, 5.30, "AM5"
        );
        cpu1.registrarRevision("Ing. Marcos Quiroga",
                "Inspección óptica de contactos LGA/AM5 bajo lupa estereoscópica y prueba de estrés Prime95 exitosa.",
                true, 0.0);

        ModuloMemoriaRam ram1 = new ModuloMemoriaRam(
                0, "RAM-DDR5-004", "Kingston", "Fury Beast 16GB", 98000.0, "OPERATIVO",
                "DIMM DDR5", 8, 60, 16, "DDR5", 5600
        );
        ram1.registrarRevision("Téc. Sofía Lucero",
                "Ejecución de 4 pasadas completas en MemTest86 v10.6 con perfil EXPO 5600MHz activo: 0 errores.",
                true, 0.0);

        DiscoAlmacenamiento ssd1 = new DiscoAlmacenamiento(
                0, "SSD-NVME-005", "Samsung", "980 PRO 1TB", 165000.0, "DEFECTUOSO",
                "PCIe 4.0 x4 NVMe M.2", 7, 60, 1000, "NVMe M.2", 64
        );
        ssd1.registrarRevision("Ing. Marcos Quiroga",
                "Lectura S.M.A.R.T. con CrystalDiskInfo arroja salud al 64% y 18 bloques reasignados. Separado para tramitar RMA.",
                false, 8500.0);

        int idPc1 = dao.crear(pc1);
        dao.crear(nb1);
        int idCpu1 = dao.crear(cpu1);
        int idRam1 = dao.crear(ram1);
        dao.crear(ssd1);

        // Vincular componentes por agregación a la Workstation PC-WS-001
        dao.vincularComponenteAEquipo(idPc1, idCpu1);
        dao.vincularComponenteAEquipo(idPc1, idRam1);
        service.sincronizarConBaseDeDatos();
    }
}
