package org.ayed.tda.grafo;

/**
 * Interfaz para una función heurística,
 * para el algoritmo A*.
 *
 * @param <T> Tipo de dato del vértice.
 */
public interface Heuristica<T> {
    /**
     * Calcula el puntaje para ir de origen a destino.
     *
     * @param origen  Vértice origen.
     * @param destino Vértice destino.
     * @return el puntaje.
     */
    int calcularPuntaje(T origen, T destino);
}
