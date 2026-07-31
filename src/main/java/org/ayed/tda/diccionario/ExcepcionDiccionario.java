package org.ayed.tda.diccionario;

public class ExcepcionDiccionario extends RuntimeException {
    /**
     * Constructor de ExcepcionDiccionario.
     * Esta clase es para el uso exclusivo de Diccionario.
     *
     * @param mensaje Mensaje de error.
     * @see Diccionario
     */
    public ExcepcionDiccionario(String mensaje) {
        super(mensaje);
    }
}
