package com.consoleolog.anyaskapiserver.global.security.handler;

import com.consoleolog.anyaskapiserver.v1.util.ApiResponse;
import com.consoleolog.anyaskapiserver.v1.util.CommonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("[CustomAuthenticationFailHandler] :: 로그인 실패");
        log.debug("[CustomAuthenticationFailHandler] :: {}", exception.getMessage());

        ApiResponse<String> apiResponse = ApiResponse.error("Fail to Login", HttpStatus.UNAUTHORIZED);
        CommonUtils.printJsonResponse(response, apiResponse);
    }
}
