package org.ayed.poe.sistemas;

import java.util.Scanner;
import org.ayed.poe.entidades.Entidad;

public class LectorTeclado {
    private Scanner entrada;

    public LectorTeclado() {
        this.entrada = new Scanner(System.in);
    }

    public int leerEntero(String mensaje) {
        int numero = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.println(mensaje);
            if (entrada.hasNextInt()) {
                numero = this.entrada.nextInt();
                this.entrada.nextLine();
                entradaValida = true;
            } else {
                System.out.println("Numero invalido");
                this.entrada.nextLine();
            }

        }

        return numero;
    }

    public int leerDireccion(String mensaje) {
        int direccion = Entidad.NO_ATACA;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.println(mensaje);

            String texto = this.entrada.nextLine().trim().toUpperCase();

            if (texto.equals("W")) {
                direccion = Entidad.ARRIBA;
                entradaValida = true;
            } else if (texto.equals("D")) {
                direccion = Entidad.DERECHA;
                entradaValida = true;
            } else if (texto.equals("S")) {
                direccion = Entidad.ABAJO;
                entradaValida = true;
            } else if (texto.equals("A")) {
                direccion = Entidad.IZQUIERDA;
                entradaValida = true;
            } else {
                System.out.println("Direccion invalida");
            }
        }

        return direccion;
    }

    public String leerTexto(String mensaje) {
        boolean entradaValida = false;
        String linea = "";

        while (!entradaValida) {
            System.out.println(mensaje);
            if (entrada.hasNextLine()) {
                linea = this.entrada.nextLine();
                if (!linea.trim().isEmpty()) {
                    entradaValida = true;
                }
            }

        }

        return linea;
    }

}
