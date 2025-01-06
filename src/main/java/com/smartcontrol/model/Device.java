package com.smartcontrol.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    public enum Status {
        ON, OFF, MAINTENANCE
    }

    public enum SensorType {
        TEMPERATURE, HUMIDITY, MOTION, LIGHT
    }
}
