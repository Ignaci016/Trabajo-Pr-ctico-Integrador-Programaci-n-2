package ar.edu.poo.inventario.util;

import ar.edu.poo.inventario.exceptions.EntradaInvalidaException;
import ar.edu.poo.inventario.model.ModuloMemoriaRam;
import java.util.Scanner;

/**
 * Clase utilitaria que aplica el principio DRY (Don't Repeat Yourself),
 * centralizando la lectura y validacion de entradas de consola y lanzando EntradaInvalidaException.
 */
public class ValidadorConsola {
    private final Scanner scanner;

    public ValidadorConsola(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerTextoNoVacio(String prompt, String nombreCampo) throws EntradaInvalidaException {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) {
            throw new EntradaInvalidaException(nombreCampo, "El valor ingresado no puede estar vacio.");
        }
        return entrada;
    }

    public int leerEnteroEnRango(String prompt, String nombreCampo, int min, int max) throws EntradaInvalidaException {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(entrada);
            if (valor < min || valor > max) {
                throw new EntradaInvalidaException(nombreCampo,
                        String.format("El numero %d esta fuera del rango permitido [%d .. %d].", valor, min, max));
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(nombreCampo, "Se esperaba un numero entero valido y se recibio '" + entrada + "'.");
        }
    }

    public double leerDoublePositivo(String prompt, String nombreCampo) throws EntradaInvalidaException {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        try {
            double valor = Double.parseDouble(entrada);
            if (valor < 0) {
                throw new EntradaInvalidaException(nombreCampo, "El monto o medida no puede ser negativo.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(nombreCampo, "Se esperaba un valor numerico decimal valido y se recibio '" + entrada + "'.");
        }
    }

    /**
     * Lee una tecnologia DDR y solo acepta DDR3, DDR4 o DDR5.
     * La entrada se normaliza a mayusculas antes de devolverla.
     */
    public String leerTecnologiaDdr(String prompt) throws EntradaInvalidaException {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        try {
            return ModuloMemoriaRam.normalizarTecnologiaDdr(entrada);
        } catch (IllegalArgumentException e) {
            throw new EntradaInvalidaException("tecnologiaDdr", e.getMessage());
        }
    }

    public String leerEstadoOperativo(String prompt) throws EntradaInvalidaException {
        System.out.print(prompt + " (1=OPERATIVO, 2=EN_REVISION, 3=DEFECTUOSO): ");
        String op = scanner.nextLine().trim();
        switch (op) {
            case "1": return "OPERATIVO";
            case "2": return "EN_REVISION";
            case "3": return "DEFECTUOSO";
            default:
                throw new EntradaInvalidaException("estadoOperativo", "Opcion de estado no valida. Seleccione 1, 2 o 3.");
        }
    }
}
