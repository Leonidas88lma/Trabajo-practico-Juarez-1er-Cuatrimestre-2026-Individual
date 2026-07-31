package org.ayed.complejidad;

import org.ayed.poe.mazmorra.Celda;
import org.ayed.poe.mazmorra.GeneradorMazmorrasAleatorias;
import org.ayed.poe.mazmorra.HeuristicaCelda;
import org.ayed.poe.mazmorra.Mazmorra;

public class MainComplejidad {

    private static final int CANTIDAD_PRUEBAS = 10000;

    public static void main(String[] args) {

        // Generar una única mazmorra para asegurar mismo grafo en todas las pruebas
        GeneradorMazmorrasAleatorias generador =
                new GeneradorMazmorrasAleatorias();

        generador.generarNivel(5);

        Mazmorra mazmorra =
                generador.getMazmorraJugable();

        // Destinos usados para variar distancia de búsqueda
        int[] destinos = {3, 6, 9, 12, 14};

        System.out.println(
                "Destino;BFS(ns);Dijkstra(ns);A*(ns);OpBFS;OpDijkstra;OpA*");

        for (int i = 0; i < destinos.length; i++) {

            Celda celdaDestino =
                    mazmorra.obtenerCelda(
                            destinos[i],
                            destinos[i]);

            // Validación de pared
            if (celdaDestino.esPared) {
                System.out.println(
                        "Saltando destino "
                                + destinos[i]
                                + ","
                                + destinos[i]
                                + " porque es pared");
                continue;
            }

            Celda origen =
                    mazmorra.obtenerCelda(0, 0);

            Celda destino =
                    celdaDestino;

            long bfs = 0;
            long dijkstra = 0;
            long aEstrella = 0;

            long operacionesBfs = 0;
            long operacionesDijkstra = 0;
            long operacionesAEstrella = 0;

            // Repeticiones para promedio estable
            for (int j = 0; j < CANTIDAD_PRUEBAS; j++) {

                bfs += medirBfs(
                        mazmorra,
                        origen,
                        destino);

                operacionesBfs +=
                        mazmorra.getGrafo()
                                .getOperacionesBfs();

                dijkstra += medirDijkstra(
                        mazmorra,
                        origen,
                        destino);

                operacionesDijkstra +=
                        mazmorra.getGrafo()
                                .getOperacionesDijkstra();

                aEstrella += medirAEstrella(
                        mazmorra,
                        origen,
                        destino);

                operacionesAEstrella +=
                        mazmorra.getGrafo()
                                .getOperacionesAEstrella();
            }

            long promedioOperacionesBfs =
                    operacionesBfs / CANTIDAD_PRUEBAS;

            long promedioOperacionesDijkstra =
                    operacionesDijkstra / CANTIDAD_PRUEBAS;

            long promedioOperacionesAEstrella =
                    operacionesAEstrella / CANTIDAD_PRUEBAS;

            // Salida final en formato CSV para Python
            System.out.println(
                    destinos[i]
                            + ";"
                            + (bfs / CANTIDAD_PRUEBAS)
                            + ";"
                            + (dijkstra / CANTIDAD_PRUEBAS)
                            + ";"
                            + (aEstrella / CANTIDAD_PRUEBAS)
                            + ";"
                            + promedioOperacionesBfs
                            + ";"
                            + promedioOperacionesDijkstra
                            + ";"
                            + promedioOperacionesAEstrella);
        }
    }

    private static long medirBfs(
            Mazmorra mazmorra,
            Celda origen,
            Celda destino) {

        // reset implícito en el algoritmo
        long inicio = System.nanoTime();

        mazmorra.getGrafo().bfs(origen, destino);

        return System.nanoTime() - inicio;
    }

    private static long medirDijkstra(
            Mazmorra mazmorra,
            Celda origen,
            Celda destino) {

        long inicio = System.nanoTime();

        mazmorra.getGrafo().dijkstra(origen, destino);

        return System.nanoTime() - inicio;
    }

    private static long medirAEstrella(
            Mazmorra mazmorra,
            Celda origen,
            Celda destino) {

        long inicio = System.nanoTime();

        mazmorra.getGrafo().aEstrella(
                origen,
                destino,
                new HeuristicaCelda());

        return System.nanoTime() - inicio;
    }
}