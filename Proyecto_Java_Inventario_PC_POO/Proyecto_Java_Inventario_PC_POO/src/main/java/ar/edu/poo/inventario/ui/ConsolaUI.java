package ar.edu.poo.inventario.ui;

import ar.edu.poo.inventario.exceptions.EntradaInvalidaException;
import ar.edu.poo.inventario.exceptions.PersistenciaException;
import ar.edu.poo.inventario.exceptions.PosicionListaInvalidaException;
import ar.edu.poo.inventario.model.*;
import ar.edu.poo.inventario.service.InventarioService;
import ar.edu.poo.inventario.util.ValidadorConsola;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario interactiva por consola.
 * Captura y valida entradas, gestiona las excepciones personalizadas y permite operar el CRUD SQLite.
 */
public class ConsolaUI {
    private final InventarioService service;
    private final Scanner scanner;
    private final ValidadorConsola validador;

    public ConsolaUI(InventarioService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
        this.validador = new ValidadorConsola(scanner);
    }

    public void iniciarMenu() {
        boolean continuar = true;
        while (continuar) {
            mostrarEncabezadoMenu();
            try {
                int opcion = validador.leerEnteroEnRango("Seleccione una opción [0-9]: ", "Opción Menú", 0, 9);
                switch (opcion) {
                    case 1 -> listarInventarioCompleto();
                    case 2 -> altaProductoInteractiva();
                    case 3 -> registrarRevisionConAclaracion();
                    case 4 -> inspeccionarFichaYRevisionesPorPosicion();
                    case 5 -> modificarPrecioYEstadoProducto();
                    case 6 -> eliminarProductoDeInventario();
                    case 7 -> buscarYFiltrarEnSQLite();
                    case 8 -> ejecutarDiagnosticosYFiltrarConIterator();
                    case 9 -> asociarComponenteAPCExistente();
                    case 0 -> {
                        System.out.println("\nCerrando conexión con SQLite. ¡Hasta luego!");
                        continuar = false;
                    }
                }
            } catch (EntradaInvalidaException | PosicionListaInvalidaException | PersistenciaException e) {
                System.out.println("\n[!] EXCEPCIÓN CONTROLADA: " + e.getMessage());
            }
        }
    }

    private void mostrarEncabezadoMenu() {
        System.out.println("\n================================================================================");
        System.out.println("   SISTEMA DE INVENTARIO DE PCs, COMPONENTES Y REVISIONES TÉCNICAS (POO + SQLite)");
        System.out.println("================================================================================");
        System.out.println(" 1. Listar inventario general (Recorrido con FOR + Cálculo de valor con WHILE)");
        System.out.println(" 2. Agregar nueva PC o Componente al inventario (CREATE en SQLite)");
        System.out.println(" 3. Registrar Revisión Técnica con aclaraciones detalladas (Composición + Sobrecarga)");
        System.out.println(" 4. Ver ficha técnica e historial de revisiones por posición (Control de Índice)");
        System.out.println(" 5. Modificar precio base o estado operativo de un producto (UPDATE en SQLite)");
        System.out.println(" 6. Eliminar un producto del inventario y sus revisiones (DELETE en SQLite)");
        System.out.println(" 7. Buscar y filtrar productos mediante consultas SQL (LIKE / Estado)");
        System.out.println(" 8. Ejecutar diagnóstico polimórfico y filtrar urgentes (Uso de ITERATOR)");
        System.out.println(" 9. Vincular un Componente en stock a una PC (Relación de AGREGACIÓN)");
        System.out.println(" 0. Salir del sistema");
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void listarInventarioCompleto() {
        System.out.println("\n--- LISTADO GENERAL DE INVENTARIO (Recorrido con bucle FOR) ---");
        List<String> filas = service.generarListadoNumeradoConFor();
        if (filas.isEmpty()) {
            System.out.println("El inventario se encuentra vacío.");
            return;
        }
        for (String fila : filas) {
            System.out.println("  " + fila);
        }
        double totalValor = service.calcularValorTotalInventarioConWhile();
        System.out.printf("--> Valor total actual del inventario (calculado con bucle WHILE): $%.2f%n", totalValor);
    }

    private void altaProductoInteractiva() throws EntradaInvalidaException, PersistenciaException {
        System.out.println("\n--- ALTA DE NUEVO PRODUCTO EN INVENTARIO ---");
        System.out.println("Categorías: 1=PC Escritorio | 2=Notebook | 3=Microprocesador | 4=Memoria RAM | 5=Disco Almacenamiento");
        int tipo = validador.leerEnteroEnRango("Seleccione tipo [1-5]: ", "Tipo Categoría", 1, 5);
        String codigo = validador.leerTextoNoVacio("Código de Serie (ej. PC-LAB-09): ", "codigoSerie");
        String marca = validador.leerTextoNoVacio("Marca (ej. Dell, Lenovo, AMD, Kingston): ", "marca");
        String modelo = validador.leerTextoNoVacio("Modelo: ", "modelo");
        double precio = validador.leerDoublePositivo("Precio Base ($): ", "precioBase");
        String estado = validador.leerEstadoOperativo("Estado Operativo inicial");

        ProductoInventario nuevo = null;
        switch (tipo) {
            case 1 -> {
                String cpu = validador.leerTextoNoVacio("Procesador instalado: ", "procesador");
                int ram = validador.leerEnteroEnRango("Memoria RAM (GB): ", "memoriaRamGb", 2, 512);
                int alm = validador.leerEnteroEnRango("Almacenamiento (GB): ", "almacenamientoGb", 64, 16384);
                int watts = validador.leerEnteroEnRango("Potencia de Fuente (Watts): ", "wattsFuente", 200, 2000);
                String gabinete = validador.leerTextoNoVacio("Factor de forma gabinete (ATX/Micro-ATX): ", "factorForma");
                int gpu = validador.leerEnteroEnRango("¿Posee GPU dedicada? (1=Sí, 0=No): ", "tieneGpu", 0, 1);
                nuevo = new PcEscritorio(0, codigo, marca, modelo, precio, estado, cpu, ram, alm, watts, gabinete, gpu == 1);
            }
            case 2 -> {
                String cpu = validador.leerTextoNoVacio("Procesador instalado: ", "procesador");
                int ram = validador.leerEnteroEnRango("Memoria RAM (GB): ", "memoriaRamGb", 4, 128);
                int alm = validador.leerEnteroEnRango("Almacenamiento SSD (GB): ", "almacenamientoGb", 128, 8192);
                double pulg = validador.leerDoublePositivo("Tamaño de pantalla (pulgadas): ", "pulgadasPantalla");
                int wh = validador.leerEnteroEnRango("Capacidad de batería (Wh): ", "capacidadBateriaWh", 20, 120);
                int ciclos = validador.leerEnteroEnRango("Ciclos de carga actuales: ", "ciclosCarga", 0, 3000);
                nuevo = new Notebook(0, codigo, marca, modelo, precio, estado, cpu, ram, alm, pulg, wh, ciclos);
            }
            case 3 -> {
                int nucleos = validador.leerEnteroEnRango("Cantidad de núcleos: ", "cantidadNucleos", 2, 128);
                double ghz = validador.leerDoublePositivo("Frecuencia Turbo (GHz): ", "frecuenciaGhz");
                String socket = validador.leerTextoNoVacio("Socket (ej. AM5, LGA1700): ", "socket");
                int tdp = validador.leerEnteroEnRango("Consumo TDP (Watts): ", "consumoWatts", 15, 350);
                nuevo = new Microprocesador(0, codigo, marca, modelo, precio, estado, "Socket " + socket, tdp, 36, nucleos, ghz, socket);
            }
            case 4 -> {
                int capGb = validador.leerEnteroEnRango("Capacidad del módulo (GB): ", "capacidadGb", 2, 256);
                String ddr = validador.leerTextoNoVacio("Tecnología (ej. DDR3 / DDR4 / DDR5): ", "tecnologiaDdr");
                int mhz = validador.leerEnteroEnRango("Frecuencia (MHz) [400-9600]: ", "frecuenciaMhz", 400, 9600);
                nuevo = new ModuloMemoriaRam(0, codigo, marca, modelo, precio, estado, "DIMM " + ddr.toUpperCase(), 10, 60, capGb, ddr.toUpperCase(), mhz);
            }
            case 5 -> {
                int capGb = validador.leerEnteroEnRango("Capacidad de unidad (GB): ", "capacidadGb", 120, 16384);
                String tec = validador.leerTextoNoVacio("Tipo (NVMe M.2 / SSD SATA / HDD): ", "tipoTecnologia");
                int smart = validador.leerEnteroEnRango("Porcentaje de salud S.M.A.R.T. [1-100]: ", "saludSmart", 1, 100);
                nuevo = new DiscoAlmacenamiento(0, codigo, marca, modelo, precio, estado, tec, 8, 36, capGb, tec, smart);
            }
        }
        int idGenerado = service.registrarProducto(nuevo);
        System.out.println("[OK] Producto registrado exitosamente en SQLite con ID #" + idGenerado);
    }

    private void registrarRevisionConAclaracion()
            throws EntradaInvalidaException, PosicionListaInvalidaException, PersistenciaException {
        listarInventarioCompleto();
        int pos = validador.leerEnteroEnRango("Ingrese la posición en lista del producto revisado: ", "Posición", -10, 999);
        // Validación explícita de posición antes de pedir el resto de los datos
        ProductoInventario seleccionado = service.obtenerPorPosicionEnLista(pos);
        System.out.println("Seleccionado: " + seleccionado.obtenerResumenEstado());

        String tecnico = validador.leerTextoNoVacio("Nombre del Técnico revisor: ", "tecnicoResponsable");
        String aclaracion = validador.leerTextoNoVacio(
                "Aclaración detallada de la revisión (pruebas realizadas, pasta térmica, hallazgos): ",
                "aclaracionDetallada");
        int ap = validador.leerEnteroEnRango("¿Revisión aprobada? (1=Aprobada/Operativo, 0=Con fallas/En Revisión): ", "aprobado", 0, 1);
        double costo = validador.leerDoublePositivo("Costo de insumos/repuestos aplicados ($): ", "costoServicio");

        service.registrarRevisionEnProducto(pos, tecnico, aclaracion, ap == 1, costo);
        System.out.println("[OK] Revisión técnica guardada y estado del equipo actualizado en SQLite.");
    }

    private void inspeccionarFichaYRevisionesPorPosicion()
            throws EntradaInvalidaException, PosicionListaInvalidaException {
        listarInventarioCompleto();
        int pos = validador.leerEnteroEnRango("Ingrese la posición de lista a inspeccionar: ", "Posición", -10, 999);
        ProductoInventario prod = service.obtenerPorPosicionEnLista(pos);
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(prod.obtenerFichaTecnica());
        System.out.println("   Diagnóstico en tiempo real: " + prod.ejecutarDiagnostico());

        if (prod instanceof EquipoComputo) {
            EquipoComputo eq = (EquipoComputo) prod;
            System.out.println("   Componentes vinculados por AGREGACIÓN (" + eq.getComponentesAgregados().size() + "):");
            for (ComponenteHardware comp : eq.getComponentesAgregados()) {
                System.out.println("     * " + comp.obtenerResumenEstado());
            }
        }

        System.out.println("   Historial de Revisiones Técnicas por COMPOSICIÓN (" + prod.getHistorialRevisiones().size() + "):");
        Iterator<RevisionTecnica> itRev = prod.getHistorialRevisiones().iterator();
        if (!itRev.hasNext()) {
            System.out.println("     (Sin revisiones técnicas registradas hasta el momento)");
        }
        while (itRev.hasNext()) {
            System.out.println("     " + itRev.next().toString());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void modificarPrecioYEstadoProducto()
            throws EntradaInvalidaException, PosicionListaInvalidaException, PersistenciaException {
        listarInventarioCompleto();
        int pos = validador.leerEnteroEnRango("Posición del producto a modificar: ", "Posición", -10, 999);
        ProductoInventario prod = service.obtenerPorPosicionEnLista(pos);
        System.out.println("Modificando: " + prod.obtenerResumenEstado());
        double nuevoPrecio = validador.leerDoublePositivo("Nuevo Precio Base ($): ", "precioBase");
        String nuevoEstado = validador.leerEstadoOperativo("Nuevo Estado Operativo");
        if (service.actualizarPrecioYEstado(pos, nuevoPrecio, nuevoEstado)) {
            System.out.println("[OK] Registro actualizado correctamente en la base de datos SQLite.");
        }
    }

    private void eliminarProductoDeInventario()
            throws EntradaInvalidaException, PosicionListaInvalidaException, PersistenciaException {
        listarInventarioCompleto();
        int pos = validador.leerEnteroEnRango("Posición del producto a eliminar: ", "Posición", -10, 999);
        if (service.eliminarProductoPorPosicion(pos)) {
            System.out.println("[OK] Producto y su historial de revisiones eliminados de SQLite.");
        }
    }

    private void buscarYFiltrarEnSQLite() throws EntradaInvalidaException, PersistenciaException {
        System.out.println("\n--- BÚSQUEDA Y FILTRADO MEDIANTE CONSULTAS SQL ---");
        System.out.println(" 1. Buscar por código de serie, marca o modelo (SELECT ... WHERE LIKE)");
        System.out.println(" 2. Filtrar por estado operativo (OPERATIVO / EN_REVISION / DEFECTUOSO)");
        int sub = validador.leerEnteroEnRango("Seleccione tipo de filtro [1-2]: ", "Filtro SQL", 1, 2);
        List<ProductoInventario> resultados;
        if (sub == 1) {
            String texto = validador.leerTextoNoVacio("Ingrese texto a buscar: ", "terminoBusqueda");
            resultados = service.buscarPorCriterioSQL(texto);
        } else {
            String est = validador.leerEstadoOperativo("Seleccione el estado a filtrar");
            resultados = service.filtrarPorEstadoSQL(est);
        }
        System.out.println("Resultados encontrados en SQLite (" + resultados.size() + "):");
        for (ProductoInventario p : resultados) {
            System.out.println("  -> " + p.obtenerResumenEstado());
        }
    }

    private void ejecutarDiagnosticosYFiltrarConIterator() {
        System.out.println("\n--- DIAGNÓSTICO POLIMÓRFICO Y FILTRADO CON ITERATOR ---");
        List<ProductoInventario> urgentes = service.filtrarMantenimientoUrgenteConIterator();
        System.out.println("Equipos/componentes que requieren mantenimiento urgente (" + urgentes.size() + "):");
        Iterator<ProductoInventario> it = urgentes.iterator();
        while (it.hasNext()) {
            ProductoInventario p = it.next();
            System.out.println("  [!] " + p.obtenerResumenEstado());
            System.out.println("      Diagnóstico: " + p.ejecutarDiagnostico());
        }
    }

    private void asociarComponenteAPCExistente()
            throws EntradaInvalidaException, PosicionListaInvalidaException, PersistenciaException {
        listarInventarioCompleto();
        int posEquipo = validador.leerEnteroEnRango("Posición del Equipo (PC Escritorio o Notebook): ", "Posición Equipo", 0, 999);
        int posComp = validador.leerEnteroEnRango("Posición del Componente Hardware a instalar/agregar: ", "Posición Componente", 0, 999);
        ProductoInventario eq = service.obtenerPorPosicionEnLista(posEquipo);
        ProductoInventario comp = service.obtenerPorPosicionEnLista(posComp);
        if (!(eq instanceof EquipoComputo)) {
            throw new EntradaInvalidaException("Equipo", "El producto en la posición " + posEquipo + " no es una PC ni Notebook.");
        }
        if (!(comp instanceof ComponenteHardware)) {
            throw new EntradaInvalidaException("Componente", "El producto en la posición " + posComp + " no es un Componente de Hardware.");
        }
        service.asociarComponenteAPC(eq.getId(), comp.getId());
        System.out.println("[OK] Componente vinculado exitosamente por AGREGACIÓN al equipo seleccionado.");
    }
}
