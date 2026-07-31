package org.ayed.tda.grafo;

import org.ayed.tda.comparador.Comparador;

class ComparadorNodoPrioridad<T>
        implements Comparador<NodoPrioridad<T>> {

    @Override
    public int comparar(
            NodoPrioridad<T> d1,
            NodoPrioridad<T> d2) {

        return Integer.compare(
                d1.prioridad,
                d2.prioridad);
    }
}