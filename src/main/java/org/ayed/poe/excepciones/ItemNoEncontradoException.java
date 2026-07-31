package org.ayed.poe.excepciones;

public class ItemNoEncontradoException extends RuntimeException {
    public ItemNoEncontradoException() {
        super();
    }

    public ItemNoEncontradoException(String message) {
        super(message);
    }
}
