package com.smartcontrol.controller;

import com.smartcontrol.dto.UserDTO;
import com.smartcontrol.dto.UserProfileUpdateDTO;
import com.smartcontrol.model.User;
import com.smartcontrol.service.TokenBlacklistService;
import com.smartcontrol.service.UserService;
import com.smartcontrol.util.TokenUtils;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    
    @PutMapping("/me")
    public ResponseEntity<User> updateUserProfile(
            @RequestHeader("Authorization") String accessToken,
            @Valid @RequestBody UserProfileUpdateDTO updateDTO) {
        
        String email = tokenUtils.extractEmail(accessToken.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        User updatedUser = userService.updateUserProfile(user.getId(), updateDTO.getName(), updateDTO.getPhone());
        return ResponseEntity.ok(updatedUser);
    }

    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> response = users.stream().map(UserDTO::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')") 
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(UserDTO.toDto(user));
    }

    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')") 
    public ResponseEntity<UserDTO> approveUser(@PathVariable Long id, @AuthenticationPrincipal User user) {
        User approvedUser = userService.approveUser(id, user);
        return ResponseEntity.ok(UserDTO.toDto(approvedUser));
    }

    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')") 
    public ResponseEntity<UserDTO> rejectUser(@PathVariable Long id, @AuthenticationPrincipal User user) {
        User rejectedUser = userService.rejectUser(id, user);
        return ResponseEntity.ok(UserDTO.toDto(rejectedUser));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @AuthenticationPrincipal User user) {
        userService.deleteUser(id, user);
        return ResponseEntity.noContent().build();
    }

    
    @PutMapping("/{userId}/assign-manager/{schoolId}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<User> assignManager(@PathVariable Long userId, @PathVariable Long schoolId) {
        User updatedUser = userService.assignManager(schoolId, userId);
        return ResponseEntity.ok(updatedUser);
    }

    
    @GetMapping(value = "/me", produces = "application/json; charset=utf8")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String accessToken) {
        String email = tokenUtils.extractEmail(accessToken.replace("Bearer ", ""));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(UserDTO.toDto(user));
    }

    @PostMapping(value = "/logout", produces = "application/json; charset=utf8")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        Claims claims = tokenUtils.validateToken(accessToken);
        long expirationTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        tokenBlacklistService.addToBlacklist(accessToken, expirationTime);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/school-members")
    @PreAuthorize("hasRole('MANAGER')") 
    public ResponseEntity<Map<String, List<UserDTO>>> getSchoolMembers(
            @AuthenticationPrincipal User manager) {
        
        if (manager.getSchool() == null) {
            throw new IllegalArgumentException("소속된 학교가 없습니다.");
        }

        Long schoolId = manager.getSchool().getId();

        
        List<User> members = userService.getUsersBySchool(schoolId);

        
        List<UserDTO> approvedUsers = members.stream()
                .filter(user -> user.getStatus() == User.Status.APPROVED)
                .map(UserDTO::toDto)
                .collect(Collectors.toList());

        List<UserDTO> pendingUsers = members.stream()
                .filter(user -> user.getStatus() == User.Status.PENDING)
                .map(UserDTO::toDto)
                .collect(Collectors.toList());

        List<UserDTO> rejectedUsers = members.stream()
                .filter(user -> user.getStatus() == User.Status.REJECTED)
                .map(UserDTO::toDto)
                .collect(Collectors.toList());

        
        Map<String, List<UserDTO>> response = Map.of(
                "approvedUsers", approvedUsers,
                "pendingUsers", pendingUsers,
                "rejectedUsers", rejectedUsers
        );

        return ResponseEntity.ok(response);
    }

}
