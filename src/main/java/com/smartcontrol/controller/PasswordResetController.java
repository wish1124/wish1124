package com.smartcontrol.controller;

import com.smartcontrol.dto.PasswordResetDTO;
import com.smartcontrol.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    
    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            passwordResetService.createPasswordResetToken(email);
            return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("이메일 전송에 실패했습니다.");
        }
    }

    
    @GetMapping("/password-reset/verify")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        try {
            passwordResetService.verifyToken(token);
            return ResponseEntity.ok("토큰이 유효합니다. 비밀번호를 재설정하세요.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        try {
            passwordResetService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
