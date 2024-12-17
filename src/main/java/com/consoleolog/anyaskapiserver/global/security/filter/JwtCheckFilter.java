package com.consoleolog.anyaskapiserver.global.security.filter;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.consoleolog.anyaskapiserver.v1.util.CommonUtil;
import com.consoleolog.anyaskapiserver.v1.util.CookieUtils;
import com.consoleolog.anyaskapiserver.v1.util.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.debug("------------------------- jwt check filter ------------------------");
//
//        Optional<Cookie> accessTokenOptional = CookieUtils.getCookie(request, "access_token");
//        String accessToken;
//        if (accessTokenOptional.isPresent()) {
//            accessToken = accessTokenOptional.get().getValue();
//            log.debug("access token : {}", accessToken);
//
//            if (validToken(accessToken)) {
//                filterChain.doFilter(request, response);
//            }
//
//            Claims claims = jwtProvider.extractToken(accessToken);
//
//            log.info("expire time : {}", claims.get("exp"));
//
//            Long exp = claims.get("exp", Long.class);
//            if (checkTime(exp)){
//                CookieUtils.deleteCookie(request, response, "access_token");
//                UserPrincipal user = CommonUtil.getCurrentUser();
//                String newAccessToken = jwtProvider.createAccessToken(user);
//                CookieUtils.addCookie(response, "access_token", newAccessToken, 60 * 60 * 3);
//            }
//            filterChain.doFilter(request, response);
//        } else {
//            Optional<Cookie> refreshTokenOptional = CookieUtils.getCookie(request, "refresh_token");
//            String refreshToken;
//            if (refreshTokenOptional.isPresent()) {
//                refreshToken = refreshTokenOptional.get().getValue();
//
//                if (validToken(refreshToken)) {
//                    filterChain.doFilter(request, response);
//                }
//
//                UserPrincipal user = CommonUtil.getCurrentUser();
//                String newAccessToken = jwtProvider.createAccessToken(user);
//                CookieUtils.addCookie(response, "refresh_token", newAccessToken, 60 * 60 * 3);
//
//            }
//            filterChain.doFilter(request, response);
//        }
        filterChain.doFilter(request, response);
    }

    private Boolean checkTime(Long exp){
        Date expDate = new Date(exp * 1000);
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);
        return leftMin < 10;
    }

    private Boolean validToken(String token){
        try {
            jwtProvider.extractToken(token);
        } catch (Exception e) {
            return true;
        }
        return false;
    }


}
