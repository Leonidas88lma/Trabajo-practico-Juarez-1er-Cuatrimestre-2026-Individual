package org.ayed.tda.iterador;

public class ExcepcionNoHayDato extends RuntimeException {
    /**
     * Constructor de ExcepcionNoHayDato.
     * Esta clase es para el uso exclusivo de {@code Iterador}
     * cuando se realice una operación sobre un dato no existente
     * (por ejemplo, acceder al final de la estructura).
     *
     * @param mensaje Mensaje de error.
     * @see Iterador
     */
    public ExcepcionNoHayDato(String mensaje) {
        super(mensaje);
    }
}
