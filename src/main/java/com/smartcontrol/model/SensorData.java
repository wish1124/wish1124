package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.smartcontrol.dto.SensorDataDTO;

@Entity
@Data
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Device.SensorType sensorType; 

    @Column(nullable = false)
    private double value; 

    @Column(nullable = false)
    private LocalDateTime timestamp; 
}
