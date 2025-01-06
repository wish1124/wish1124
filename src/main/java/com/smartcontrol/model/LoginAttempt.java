package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; 

    @Column(nullable = false)
    private boolean success; 

    @Column(nullable = false)
    private String ipAddress; 

    @Column(nullable = false)
    private LocalDateTime timestamp; 
}
