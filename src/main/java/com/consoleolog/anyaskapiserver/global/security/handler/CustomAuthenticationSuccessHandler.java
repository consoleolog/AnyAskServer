package com.consoleolog.anyaskapiserver.global.security.handler;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.consoleolog.anyaskapiserver.v1.util.CookieUtils;
import com.consoleolog.anyaskapiserver.v1.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("-------------------------- login success handler --------------------------");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug(user.toString());

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        CookieUtils.addCookie(response, "access_token", accessToken, 60 * 60 * 3);
        CookieUtils.addCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 3);

        if ("Y".equals(user.getSocialYn())){
            response.sendRedirect("http://localhost:8501?message=login_success");
        }

    }
}
