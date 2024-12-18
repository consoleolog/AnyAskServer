package com.consoleolog.anyaskapiserver.global.security.handler;

import com.consoleolog.anyaskapiserver.v1.util.ApiResponse;
import com.consoleolog.anyaskapiserver.v1.util.CommonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn("[CustomAccessDeniedHandler] :: {}", accessDeniedException.getMessage());
        log.warn("[CustomAccessDeniedHandler] :: {}", request.getRequestURI());
        log.warn("[CustomAccessDeniedHandler] :: 접근 권한이 없습니다.");

        ApiResponse<?> result = ApiResponse.error("You don't have permission", HttpStatus.FORBIDDEN);
        CommonUtils.printJsonResponse(response, result);
    }
}
