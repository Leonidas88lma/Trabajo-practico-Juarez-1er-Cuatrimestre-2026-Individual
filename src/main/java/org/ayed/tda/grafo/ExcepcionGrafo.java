package org.ayed.tda.grafo;

public class ExcepcionGrafo extends RuntimeException {
    /**
     * Constructor de ExcepcionGrafo.
     * Esta clase es para el uso exclusivo de Grafo.
     *
     * @param mensaje Mensaje de error.
     * @see Grafo
     */
    public ExcepcionGrafo(String mensaje) {
        super(mensaje);
    }
}
