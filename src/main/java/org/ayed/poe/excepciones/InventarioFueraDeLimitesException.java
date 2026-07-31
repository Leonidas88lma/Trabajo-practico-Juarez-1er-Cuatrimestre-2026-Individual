package org.ayed.poe.excepciones;

public class InventarioFueraDeLimitesException extends RuntimeException {
    public InventarioFueraDeLimitesException() {
        super();
    }

    public InventarioFueraDeLimitesException(String message) {
        super(message);
    }
}
