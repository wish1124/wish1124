package com.smartcontrol.service;

import com.smartcontrol.model.User;
import com.smartcontrol.repository.UserRepository;
import com.smartcontrol.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    
    public Map<String, String> authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        
        if (user.getStatus() == User.Status.REJECTED) {
            throw new IllegalArgumentException("계정이 잠겼습니다. 관리자에게 문의하세요.");
        } else if (user.getStatus() == User.Status.PENDING) {
            throw new IllegalArgumentException("아직 승인 대기 중입니다.");
        }

        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", tokenUtils.generateAccessToken(user.toUserDetails()));
        tokens.put("refreshToken", tokenUtils.generateRefreshToken(user.getEmail()));
        return tokens;
    }

    
    public Map<String, String> refreshTokens(String refreshToken) {
        String email = tokenUtils.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", tokenUtils.generateAccessToken(user.toUserDetails()));
        tokens.put("refreshToken", tokenUtils.generateRefreshToken(email));
        return tokens;
    }
    
    public boolean validateToken(String token) {
        try {
            
            tokenUtils.validateToken(token);
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }
    
}
