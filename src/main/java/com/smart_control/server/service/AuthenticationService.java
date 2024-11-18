package com.smart_control.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart_control.server.model.User;
import com.smart_control.server.repository.UserRepository;
import com.smart_control.server.util.JwtUtil;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("일치하는 계정이 없습니다."));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
