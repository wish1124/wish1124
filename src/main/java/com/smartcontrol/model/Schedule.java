package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device; 

    @Column(nullable = false)
    private String action; 

    @Column(nullable = false)
    private LocalDateTime scheduledTime; 

    @Column
    private boolean isExecuted; 
}
