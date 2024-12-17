package com.consoleolog.anyaskapiserver.v1.util;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final SecretKey key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtSecret == null ? "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword" : jwtSecret)
    );

    @Value("${jwt.expires.access_token}")
    private Long accessTokenExpiresIn;

    @Value("${jwt.expires.refresh_token}")
    private Long refreshTokenExpiresIn;

    public String createAccessToken(UserPrincipal userPrincipal) {
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("userEmail", userPrincipal.getUserEmail())
                .claim("lastLoginAt", userPrincipal.getLastLoginAt().toString())
                .claim("useYn", userPrincipal.getUseYn())
                .claim("authority", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiresIn))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("mId", userPrincipal.getMId())
                .claim("userId", userPrincipal.getUserId())
                .claim("userEmail", userPrincipal.getUserEmail())
                .claim("displayName", userPrincipal.getDisplayName())
                .claim("useYn", userPrincipal.getUseYn())
                .claim("socialYn", userPrincipal.getSocialYn())
                .claim("lastLoginAt", userPrincipal.getLastLoginAt().toString())
                .claim("createdAt", userPrincipal.getCreatedAt().toString())
                .claim("updatedAt", userPrincipal.getUpdatedAt().toString())
                .claim("authority", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiresIn))
                .signWith(key)
                .compact();
    }

    public Claims extractToken(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }


}
