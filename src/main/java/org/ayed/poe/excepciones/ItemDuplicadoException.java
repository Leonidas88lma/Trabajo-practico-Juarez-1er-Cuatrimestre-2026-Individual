package org.ayed.poe.excepciones;

public class ItemDuplicadoException extends RuntimeException {
    public ItemDuplicadoException() {
        super();
    }

    public ItemDuplicadoException(String message) {
        super(message);
    }
}
