package com.consoleolog.anyaskapiserver.global.security.handler;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.consoleolog.anyaskapiserver.v1.util.ApiResponse;
import com.consoleolog.anyaskapiserver.v1.util.CommonUtils;
import com.consoleolog.anyaskapiserver.v1.util.CookieUtils;
import com.consoleolog.anyaskapiserver.v1.util.JwtProvider;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("[CustomAuthenticationSuccessHandler] :: 로그인 성공");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("[CustomAuthenticationSuccessHandler] :: UserPrincipal : {}", user);

        log.debug("[CustomAuthenticationSuccessHandler] :: JWT 생성");
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        log.debug("[CustomAuthenticationSuccessHandler] :: accessToken : {}", accessToken);
        log.debug("[CustomAuthenticationSuccessHandler] :: refreshToken : {}", refreshToken);

        CookieUtils.addCookie(response, "access_token", accessToken, 60 * 60 * 3);
        CookieUtils.addCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 3);

        Map<String, Object> result = new HashMap<>();
        result.put("msg", "success");

        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.success(result);

        CommonUtils.printJsonResponse(response, apiResponse);
    }
}
