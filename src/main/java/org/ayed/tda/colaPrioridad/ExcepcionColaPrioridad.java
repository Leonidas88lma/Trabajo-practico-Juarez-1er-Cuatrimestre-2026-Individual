package org.ayed.tda.colaPrioridad;

public class ExcepcionColaPrioridad extends RuntimeException {
    /**
     * Constructor de ExcepcionColaPrioridad.
     * Esta clase es para el uso exclusivo de ColaPrioridad.
     *
     * @param mensaje Mensaje de error.
     * @see ColaPrioridad
     */
    public ExcepcionColaPrioridad(String mensaje) {
        super(mensaje);
    }
}
