package com.smart_control.server.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_control.server.model.RefreshToken;
import com.smart_control.server.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final long REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_DURATION));

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .filter(refreshToken -> refreshToken.getExpiryDate().isAfter(Instant.now()))
            .isPresent();
    }

    public void deleteRefreshTokenByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
    
}
