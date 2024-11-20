package com.smart_control.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "SmArtCoNtRoL2)2$1@3";
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    public static String generateAccessToken(String username, Long schoolId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("schoolId", schoolId);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
    }

    public static String generateRefreshToken(String username, Long schoolId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("schoolId", schoolId);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
    }

    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

    }

    public static String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public static Long extractSchoolId(String token) {
        return getClaims(token).get("schoolId", Long.class);
    }


    public static boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
