package com.consoleolog.anyaskapiserver.global.error.exception;

public class CustomJwtException extends ValidationException {
    public CustomJwtException(String message) {
        super(message);
    }
}
