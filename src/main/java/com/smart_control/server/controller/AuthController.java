package com.smart_control.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.RefreshToken;
import com.smart_control.server.model.User;
import com.smart_control.server.service.AuthenticationService;
import com.smart_control.server.service.RefreshTokenService;
import com.smart_control.server.util.JwtUtil;

import io.jsonwebtoken.Claims;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// 1. register
// 2. login
// 3. refresh - token

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String accessToken = authService.login(username, password);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken.getToken());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> rereshToken(@RequestBody Map<String, String> tokenMap) {
        String refreshToken = tokenMap.get("refreshToken");
        Claims claims = JwtUtil.validateToken(refreshToken);
        String username = claims.getSubject();
        Long schoolId = claims.get("schoolId", Long.class);

        String newAccessToken = JwtUtil.generateAccessToken(username, schoolId);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }
    
}
