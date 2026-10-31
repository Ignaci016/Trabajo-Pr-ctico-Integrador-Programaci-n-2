package ar.edu.poo.inventario.util;

import ar.edu.poo.inventario.exceptions.EntradaInvalidaException;
import java.util.Scanner;

/**
 * Clase utilitaria que aplica el principio DRY (Don't Repeat Yourself),
 * centralizando la lectura y validación de entradas de consola y lanzando EntradaInvalidaException.
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
            throw new EntradaInvalidaException(nombreCampo, "El valor ingresado no puede estar vacío.");
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
                        String.format("El número %d está fuera del rango permitido [%d .. %d].", valor, min, max));
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(nombreCampo, "Se esperaba un número entero válido y se recibió '" + entrada + "'.");
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
            throw new EntradaInvalidaException(nombreCampo, "Se esperaba un valor numérico decimal válido y se recibió '" + entrada + "'.");
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
                throw new EntradaInvalidaException("estadoOperativo", "Opción de estado no válida. Seleccione 1, 2 o 3.");
        }
    }
}
