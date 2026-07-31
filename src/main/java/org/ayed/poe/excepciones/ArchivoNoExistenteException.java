package org.ayed.poe.excepciones;

public class ArchivoNoExistenteException extends RuntimeException {
    public ArchivoNoExistenteException() {
        super();
    }

    public ArchivoNoExistenteException(String message) {
        super(message);
    }
}
