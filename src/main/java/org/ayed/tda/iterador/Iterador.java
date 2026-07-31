package org.ayed.tda.iterador;

public interface Iterador<T> {
    /**
     * Obtiene el dato actual del iterador.
     *
     * @return el dato actual.
     * @throws ExcepcionNoHayDato si no hay dato a acceder
     *                            (si el iterador está al final).
     */
    T dato();

    /**
     * Evalúa si se puede avanzar el iterador.
     *
     * @return true si se puede avanzar.
     */
    boolean haySiguiente();

    /**
     * Avanza el iterador.
     *
     * @throws ExcepcionNoHayDato si no hay dato siguiente.
     */
    void siguiente();

    /**
     * Evalúa si se puede retroceder el iterador.
     *
     * @return true si se puede retroceder.
     */
    boolean hayAnterior();

    /**
     * Retrocede el iterador.
     *
     * @throws ExcepcionNoHayDato si no hay dato anterior.
     */
    void anterior();

    /**
     * Agrega el dato indicado por parámetro antes
     * de la posición actual del iterador.
     * El iterador queda posicionado en la posición siguiente.
     * <p>
     * Ejemplos:
     * <pre>
     * {@code
     * >> [0, 1, 5, 3]
     * >>     ^
     * agregar(4);
     * >> [0, 4, 1, 5, 3]
     * >>        ^
     * }
     * </pre>
     * <pre>
     * {@code
     * >> [0, 1, 5, 3]
     * >>              ^ (final)
     * agregar(7);
     * >> [0, 1, 5, 3, 7]
     * >>                 ^
     * }
     * </pre>
     *
     * @param dato Dato a agregar.
     * @throws ExcepcionOperacionNoPermitida si la estructura no permite la operación.
     */
    void agregar(T dato);

    /**
     * Modifica el dato actual del iterador
     * por el indicado por parámetro.
     *
     * @throws ExcepcionNoHayDato si no hay dato a acceder
     *                            (si el iterador está al final).
     */
    void modificarDato(T dato);

    /**
     * Elimina el dato actual del iterador.
     * El iterador queda posicionado en la posición actual.
     * <p>
     * Ejemplos:
     * <pre>
     * {@code
     * >> [0, 1, 5, 3]
     * >>     ^
     * eliminar();
     * >> [0, 5, 3]
     * >>     ^
     * }
     * </pre>
     * <pre>
     * {@code
     * >> [0, 1, 5, 3]
     * >>           ^
     * eliminar();
     * >> [0, 1, 5]
     * >>           ^ (final)
     * }
     * </pre>
     *
     * @return el dato eliminado.
     * @throws ExcepcionNoHayDato si no hay dato a eliminar
     *                            (si el iterador está al final).
     */
    T eliminar();
}
