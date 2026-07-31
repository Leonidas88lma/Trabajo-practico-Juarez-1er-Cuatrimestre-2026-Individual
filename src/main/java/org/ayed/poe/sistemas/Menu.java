package org.ayed.poe.sistemas;

import java.util.Scanner;

public class Menu {
    private Scanner entrada;

    public Menu() {
        this.entrada = new Scanner(System.in);
    }

    public char mostrarMenuPrincipal() {
        char opcion;
        char[] opcionesPermitidas = { '1', '2', '3' };

        System.out.println(
                "██████████ ███████    ████████         ██                             ████     ████                                                                      \r\n"
                        + "░░░░░██░░░ ░██░░░░██  ██░░░░░░██       ░██                            ░██░██   ██░██                                                                      \r\n"
                        + "    ░██    ░██   ░██ ██      ░░        ░██        ██████    ██████    ░██░░██ ██ ░██  ██████   ██████ ██████████   ██████  ██████ ██████  ██████    ██████\r\n"
                        + "    ░██    ░███████ ░██                ░██       ░░░░░░██  ██░░░░     ░██ ░░███  ░██ ░░░░░░██ ░░░░██ ░░██░░██░░██ ██░░░░██░░██░░█░░██░░█ ░░░░░░██  ██░░░░ \r\n"
                        + "    ░██    ░██░░░░  ░██    █████ ██    ░██        ███████ ░░█████     ░██  ░░█   ░██  ███████    ██   ░██ ░██ ░██░██   ░██ ░██ ░  ░██ ░   ███████ ░░█████ \r\n"
                        + "    ░██    ░██      ░░██  ░░░░██░░     ░██       ██░░░░██  ░░░░░██    ░██   ░    ░██ ██░░░░██   ██    ░██ ░██ ░██░██   ░██ ░██    ░██    ██░░░░██  ░░░░░██\r\n"
                        + "    ░██    ░██       ░░████████  ██    ░████████░░████████ ██████     ░██        ░██░░████████ ██████ ███ ░██ ░██░░██████ ░███   ░███   ░░████████ ██████ \r\n"
                        + "    ░░     ░░         ░░░░░░░░  ░░     ░░░░░░░░  ░░░░░░░░ ░░░░░░      ░░         ░░  ░░░░░░░░ ░░░░░░ ░░░  ░░  ░░  ░░░░░░  ░░░    ░░░     ░░░░░░░░ ░░░░░░");
        System.out.println("=================");
        System.out.println("1 - Nueva Partida");
        System.out.println("2 - Cargar Partida");
        System.out.println("3 - Salir");
        System.out.println("=================");
        opcion = validarOpcion(opcionesPermitidas);

        return opcion;
    }

    public char mostrarMenuJuego() {
        char opcion = ' ';
        System.out.println("w - Arriba");
        System.out.println("a - Izquierda");
        System.out.println("s - Abajo");
        System.out.println("d - Derecha");
        System.out.println("i - Inventario");
        System.out.println("j - Atacar");
        System.out.println("x - Salir");
        char[] opcionesPermitidas = { 'W', 'A', 'S', 'D', 'I', 'J', 'X' };
        opcion = this.validarOpcion(opcionesPermitidas);

        return opcion;
    }

    public char mostrarMenuEscondite() {
        char opcion = ' ';
        System.out.println("1 - Ir a mi inventario personal");
        System.out.println("2 - Depositar item en el alijo");
        System.out.println("3 - Retirar item del alijo");
        System.out.println("4 - Retirar toda la pagina al inventario");
        System.out.println("5 - Pagina anterior del alijo");
        System.out.println("6 - Pagina siguiente del alijo");
        System.out.println("7 - Comprar nueva pagina ($250)");
        System.out.println("8 - Guardar partida");
        System.out.println("9 - Siguiente nivel");
        System.out.println("h - Habilidades");
        System.out.println("0 - Volver al inicio");
        char[] opcionesPermitidas = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'H', '0' };
        opcion = this.validarOpcion(opcionesPermitidas);

        return opcion;
    }

    public char mostrarMenuInventario() {
        char opcion = ' ';
        System.out.println("1 - Mostrar items");
        System.out.println("2 - Ver estadisticas");
        System.out.println("3 - Ver equipo");
        System.out.println("4 - Equipar item");
        System.out.println("5 - Desequipar item");
        System.out.println("6 - Salir");
        char[] opcionesPermitidas = { '1', '2', '3', '4', '5', '6' };
        opcion = this.validarOpcion(opcionesPermitidas);

        return opcion;
    }

    public char mostrarMenuArbol() {
        char opcion = ' ';
        System.out.println("1-Asignar Pasiva Directa");
        System.out.println("2-Asignar por Camino Optimo");
        System.out.println("3-Desasignar Pasiva");
        System.out.println("4-Volver");
        System.out.println();
        char[] opcionesPermitidas = { '1', '2', '3', '4' };
        opcion = this.validarOpcion(opcionesPermitidas);

        return opcion;
    }

    private char validarOpcion(char[] opciones) {
        boolean opcionValida = false;
        String entradaUsuario = "";
        char opcionElegida = ' ';

        while (!opcionValida) {
            System.out.println("Elegi una opcion");
            entradaUsuario = this.entrada.nextLine().trim().toUpperCase();

            if (entradaUsuario.length() > 0) {
                opcionElegida = entradaUsuario.charAt(0);
            }

            for (int i = 0; i < opciones.length; i++) {
                if (opcionElegida == opciones[i]) {
                    opcionValida = true;
                }
            }

        }

        if (!opcionValida) {
            System.out.println("Opcion invalida");
        }

        return opcionElegida;
    }

}