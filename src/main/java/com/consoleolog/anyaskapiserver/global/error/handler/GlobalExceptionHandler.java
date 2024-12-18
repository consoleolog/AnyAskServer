package com.consoleolog.anyaskapiserver.global.error.handler;

import com.consoleolog.anyaskapiserver.global.error.exception.BaseException;
import com.consoleolog.anyaskapiserver.v1.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
/**
 * @FileName		: GlobalExceptionHandler.java
 * @Author			: ACR
 * @Date			: 24. 12. 18.
 * @Description		: 전역 ERROR Controller
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String, Object>> handler(Exception e) {

        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Map<String, Object>> handler(BaseException e) {
        log.warn("===========================");
        log.warn("BaseException: {}", e.getMessage());
        log.warn("===========================");

        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
    }



}
