package org.ayed.tda.iterador;

public class ExcepcionOperacionNoPermitida extends RuntimeException {
    /**
     * Constructor de ExcepcionOperacionNoPermitida.
     * Esta clase es para el uso exclusivo de {@code Iterador}
     * cuando la estructura no permita una operación
     * (por ejemplo, si no admite más datos).
     *
     * @param mensaje Mensaje de error.
     * @see Iterador
     */
    public ExcepcionOperacionNoPermitida(String mensaje) {
        super(mensaje);
    }
}
