package com.smartcontrol.controller;

import com.smartcontrol.dto.UserDTO;
import com.smartcontrol.model.User;
import com.smartcontrol.service.AuthService;
import com.smartcontrol.service.TokenBlacklistService;
import com.smartcontrol.service.UserService;
import com.smartcontrol.util.TokenUtils;

import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    
    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setSchool(userDTO.getSchoolId() != null ? userService.getSchoolById(userDTO.getSchoolId()) : null);

        User createdUser = userService.createUser(user);
        UserDTO response = UserDTO.toDto(createdUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request) {
        
        String clientIp = request.getRemoteAddr();

        
        Map<String, String> tokens = authService.authenticate(email, password);

        
        userService.recordLoginAttempt(email, true, clientIp);

        return ResponseEntity.ok(tokens);
    }

    
    @PostMapping(value = "/refresh", produces = "application/json; charset=utf8")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        
        refreshToken = refreshToken.replace("Bearer ", "");
        Map<String, String> tokens = authService.refreshTokens(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping(value = "/validate", produces = "application/json; charset=utf8")
    public ResponseEntity<Map<String, String>> validateAccessToken(@RequestHeader("Authorization") String accessToken) {
        Map<String, String> response = new HashMap<>();
        try {
            
            accessToken = accessToken.replace("Bearer ", "");

            
            boolean isValid = authService.validateToken(accessToken);

            if (isValid) {
                response.put("message", "유효한 토큰입니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "유효하지 않은 토큰입니다.");
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("message", "토큰 검증 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
