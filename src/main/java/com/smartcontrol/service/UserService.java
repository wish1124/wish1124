package com.smartcontrol.service;

import com.smartcontrol.dto.UserDTO;
import com.smartcontrol.exception.ResourceNotFoundException;
import com.smartcontrol.model.LoginAttempt;
import com.smartcontrol.model.School;
import com.smartcontrol.model.User;
import com.smartcontrol.model.User.Role;
import com.smartcontrol.repository.LoginAttemptRepository;
import com.smartcontrol.repository.SchoolRepository;
import com.smartcontrol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SchoolRepository schoolRepository;

    @Autowired
    public LoginAttemptRepository loginAttemptRepository;
    
    @Autowired
    private ActivityLogService activityLogService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        validatePasswordStrength(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        user.setStatus(User.Status.PENDING);
        return userRepository.save(user);
    }

    
    public User register(String email, String password, String name, Long schoolId) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        validatePasswordStrength(password);
        School school = getSchoolById(schoolId);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setSchool(school);
        user.setStatus(User.Status.PENDING);
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    
    public User updateUserProfile(Long userId, String name, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (phone != null && !phone.isBlank()) {
            user.setPhone(phone);
        }

        return userRepository.save(user);
    }

    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        validatePasswordStrength(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        validatePasswordStrength(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().filter(u -> !u.getRole().equals(Role.ADMIN)).toList();
    }

    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    
    public School getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("학교를 찾을 수 없습니다."));
    }

    
    public User approveUser(Long userId, User me) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getSchool().getId() != me.getSchool().getId()) {
            throw new IllegalArgumentException("같은 학교 소속이 아닙니다. 권한이 없습니다.");
        }
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    
    public User rejectUser(Long userId, User me) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getSchool().getId() != me.getSchool().getId()) {
            throw new IllegalArgumentException("같은 학교 소속이 아닙니다. 권한이 없습니다.");
        }
        user.setStatus(User.Status.REJECTED);
        return userRepository.save(user);
    }

    
    public void deleteUser(Long userId, User me) {
        User user = getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getSchool().getId() == me.getSchool().getId()) {
            throw new IllegalArgumentException("같은 학교 소속이 아닙니다. 권한이 없습니다.");
        }
        userRepository.deleteById(userId);
    }

    
    public void checkLoginAttempts(String email) {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<LoginAttempt> recentAttempts = loginAttemptRepository.findByEmailAndTimestampAfter(email, fiveMinutesAgo);

        long failedAttempts = recentAttempts.stream().filter(attempt -> !attempt.isSuccess()).count();

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setStatus(User.Status.REJECTED); 
                userRepository.save(user);
                throw new IllegalArgumentException("계정이 잠겼습니다. 관리자에게 문의하세요.");
            }
        }
    }

    
    public void validatePasswordStrength(String password) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자 및 특수문자를 포함해야 합니다.");
        }
    }

    
    public void recordLoginAttempt(String email, boolean success, String ipAddress) {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setEmail(email);
        loginAttempt.setSuccess(success);
        loginAttempt.setIpAddress(ipAddress);
        loginAttempt.setTimestamp(LocalDateTime.now());

        loginAttemptRepository.save(loginAttempt);

        activityLogService.createLog(email, success ? "LOGIN_SUCCESS" : "LOGIN_FAILED", ipAddress);
    }

    
    public User assignManager(Long schoolId, Long userId) {
        
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));

        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() == User.Role.ADMIN) {
            throw new IllegalArgumentException("매니저로 지정할 사용자는 일반 사용자여야 합니다.");
        }

        
        userRepository.findByRoleAndSchool(User.Role.MANAGER, school)
                .ifPresent(existingManager -> {
                    existingManager.setRole(User.Role.USER);
                    userRepository.save(existingManager);
                });

        
        user.setRole(User.Role.MANAGER);
        user.setStatus(User.Status.APPROVED);
        user.setSchool(school);

        return userRepository.save(user);
    }
    
    public List<User> getUsersBySchool(Long schoolId) {
        return userRepository.findBySchoolId(schoolId);
    }
}
