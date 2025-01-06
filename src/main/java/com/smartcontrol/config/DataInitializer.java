package com.smartcontrol.config;

import com.smartcontrol.model.User;
import com.smartcontrol.model.User.Role;
import com.smartcontrol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        String adminEmail = "admin@gmail.com";
        String adminPassword = "Smartcontrol2024!@#";

        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setName("System Administrator");
            admin.setRole(Role.ADMIN);
            admin.setStatus(User.Status.APPROVED);

            userRepository.save(admin);
            System.out.println("초기 관리자 계정 생성: " + adminEmail);
        } else {
            System.out.println("관리자 계정이 이미 존재합니다.");
        }
    }
}
