package com.consoleolog.anyaskapiserver.global.error.exception;

import java.time.LocalDateTime;

public class BaseException extends RuntimeException {
    private final LocalDateTime timestamp = LocalDateTime.now();
    public BaseException(String message) {
        super(message);
    }
}
