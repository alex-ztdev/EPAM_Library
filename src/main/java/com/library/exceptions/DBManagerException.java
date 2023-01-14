package com.library.exceptions;

public class DBManagerException extends RuntimeException {
    public DBManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBManagerException(Throwable cause) {
        super(cause);
    }
}
