package com.consoleolog.anyaskapiserver.v1.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final HttpStatus statusCode;

    private final String message;

    private final T data;

    @Builder
    public ApiResponse(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .statusCode(status)
                .message(message)
                .data(null)
                .build();
    }

    public Integer getStatus(){
        return statusCode.value();
    }

}
