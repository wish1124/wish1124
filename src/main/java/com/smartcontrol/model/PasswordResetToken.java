package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(nullable = false)
    private LocalDateTime expirationDate; 
}
