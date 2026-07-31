package org.ayed.tda.comparador;

public interface Comparador<T> {
    /**
     * Compara los datos indicados por parámetro.
     *
     * @param dato1 Primer dato a comparar.
     * @param dato2 Segundo dato a comparar.
     * @return -1 si el primer dato es menor que el segundo,
     * 0 si son iguales,
     * o 1 si el primer dato es mayor que el segundo.
     */
    int comparar(T dato1, T dato2);
}
