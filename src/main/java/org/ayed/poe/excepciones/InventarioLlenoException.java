package org.ayed.poe.excepciones;

public class InventarioLlenoException extends RuntimeException {
    public InventarioLlenoException() {
        super();
    }

    public InventarioLlenoException(String message) {
        super(message);
    }
}
