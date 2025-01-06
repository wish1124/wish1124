package com.smartcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    
    

    public void addToBlacklist(String token, long expirationTime) {
        
    }

    public boolean isBlacklisted(String token) {
        return false;
        
    }
}
