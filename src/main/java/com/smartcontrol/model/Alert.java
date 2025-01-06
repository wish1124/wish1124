package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status; 

    @Column(nullable = false)
    private LocalDateTime timestamp; 

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device; 

    public enum AlertStatus {
        NEW, VIEWED
    }
}
