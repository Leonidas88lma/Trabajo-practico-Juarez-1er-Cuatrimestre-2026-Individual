package org.ayed.tda.lista;

public class ExcepcionLista extends RuntimeException {
    /**
     * Constructor de ExcepcionLista.
     * Esta clase es para el uso exclusivo del paquete lista.
     *
     * @param mensaje Mensaje de error.
     * @see Lista
     */
    public ExcepcionLista(String mensaje) {
        super(mensaje);
    }
}
