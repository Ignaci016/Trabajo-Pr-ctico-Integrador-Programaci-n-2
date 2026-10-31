Sistema de Gestión de Inventario de PCs, Componentes y Revisiones Técnicas
Trabajo Práctico Integrador — Programación Orientada a Objetos (Java + SQLite)

Requisitos Cumplidos

Dos niveles de herencia con clases abstractas y concretas:
Nivel 0 (Abstracta): ProductoInventario
Nivel 1 (Abstractas): EquipoComputo y ComponenteHardware
Nivel 2 (Concretas): PcEscritorio, Notebook, Microprocesador, ModuloMemoriaRam, DiscoAlmacenamiento
Interfaces y Polimorfismo: Inventariable, Diagnosticable, IRepositorioCRUD<T>
Sobrecarga de métodos (Overloading): tres variantes de registrarRevision(...) y dos variantes de calcularValorActual(...) en ProductoInventario.
Composición y Agregación:
Composición: ProductoInventario ◆── RevisionTecnica (con ON DELETE CASCADE en SQLite).
Agregación: EquipoComputo ◇── ComponenteHardware (componentes independientes vinculados a un equipo).
Excepciones personalizadas: EntradaInvalidaException, PosicionListaInvalidaException, PersistenciaException.
Recorridos de listas: for clásico y for-each, while e Iterator<T> para remoción y filtrado seguro.
Persistencia SQLite CRUD + Búsquedas SQL: implementada con sqlite-jdbc, PreparedStatement, Patrón Singleton (ConexionSQLite), Patrón DAO (ProductoDAO) y Patrón Factory Method (ProductoFactory).

Ejecución con Maven
mvn clean compile exec:java
