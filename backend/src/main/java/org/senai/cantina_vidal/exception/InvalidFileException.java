package org.senai.cantina_vidal.exception;

import java.io.Serial;

public class InvalidFileException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidFileException(String message) {
        super(message);
    }
}
