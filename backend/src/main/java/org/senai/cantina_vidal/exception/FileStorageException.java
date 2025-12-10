package org.senai.cantina_vidal.exception;

import java.io.Serial;

public class FileStorageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileStorageException(String message) {
        super(message);
    }


    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
