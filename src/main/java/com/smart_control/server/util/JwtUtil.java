package com.smart_control.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private String secretKey = "SmArtCoNtRoL2)2$1@3";
    private long expirationTime = 86400000;

    public String generateToken(String username, Long schoolId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("schoolId", schoolId);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractCompanyId(String token) {
        return getClaims(token).get("schoolId", Long.class);
    }


    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
