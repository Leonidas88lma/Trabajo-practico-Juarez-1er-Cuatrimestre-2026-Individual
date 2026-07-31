package org.ayed.poe.excepciones;

public class EspacioOcupadoException extends RuntimeException {
    public EspacioOcupadoException() {
        super();
    }

    public EspacioOcupadoException(String message) {
        super(message);
    }
}
