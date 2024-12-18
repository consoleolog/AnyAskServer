package com.consoleolog.anyaskapiserver.global.error.exception;

import java.time.LocalDateTime;

/**
 * @FileName		: BaseException.java
 * @Author			: ACR
 * @Date			: 24. 12. 18.
 * @Description		: 기본 Exception
 **/
public class BaseException extends RuntimeException {
    private final LocalDateTime timestamp = LocalDateTime.now();
    public BaseException(String message) {
        super(message);
    }
}
