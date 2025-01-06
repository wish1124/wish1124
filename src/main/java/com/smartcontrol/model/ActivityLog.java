package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail; 

    @Column(nullable = false)
    private String action; 

    @Column(nullable = false)
    private String ipAddress; 

    @Column(nullable = false)
    private LocalDateTime timestamp; 
}
