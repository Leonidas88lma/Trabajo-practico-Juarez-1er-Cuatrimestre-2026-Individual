package org.ayed.tda.conjunto;

public class ExcepcionConjunto extends RuntimeException {
    /**
     * Constructor de ExcepcionConjunto.
     * Esta clase es para el uso exclusivo de Conjunto.
     *
     * @param mensaje Mensaje de error.
     * @see Conjunto
     */
    public ExcepcionConjunto(String mensaje) {
        super(mensaje);
    }
}
