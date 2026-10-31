# Sistema de Gestión de Inventario de PCs, Componentes y Revisiones Técnicas

**Trabajo Práctico Integrador — Programación Orientada a Objetos (Java + SQLite)**

## Requisitos Cumplidos
1. **Dos niveles de herencia con clases abstractas y concretas**:
   - Nivel 0 (Abstracta): `ProductoInventario`
   - Nivel 1 (Abstractas): `EquipoComputo` y `ComponenteHardware`
   - Nivel 2 (Concretas): `PcEscritorio`, `Notebook`, `Microprocesador`, `ModuloMemoriaRam`, `DiscoAlmacenamiento`
2. **Interfaces y Polimorfismo**: `Inventariable`, `Diagnosticable`, `IRepositorioCRUD<T>`
3. **Sobrecarga de métodos (Overloading)**: tres variantes de `registrarRevision(...)` y dos variantes de `calcularValorActual(...)` en `ProductoInventario`.
4. **Composición y Agregación**:
   - **Composición**: `ProductoInventario` ◆── `RevisionTecnica` (con `ON DELETE CASCADE` en SQLite).
   - **Agregación**: `EquipoComputo` ◇── `ComponenteHardware` (componentes independientes vinculados a un equipo).
5. **Excepciones personalizadas**: `EntradaInvalidaException`, `PosicionListaInvalidaException`, `PersistenciaException`.
6. **Recorridos de listas**: `for` clásico y `for-each`, `while` e `Iterator<T>` para remoción y filtrado seguro.
7. **Persistencia SQLite CRUD + Búsquedas SQL**: implementada con `sqlite-jdbc`, `PreparedStatement`, Patrón Singleton (`ConexionSQLite`), Patrón DAO (`ProductoDAO`) y Patrón Factory Method (`ProductoFactory`).

## Ejecución con Maven
```bash
mvn clean compile exec:java
```
