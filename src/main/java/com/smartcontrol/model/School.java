package com.smartcontrol.model;

import com.smartcontrol.dto.SchoolDTO;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; 

    @Column(nullable = false)
    private String address; 

    @OneToOne
    @JoinColumn(name = "manager_id")
    private User manager; 

    private boolean isActive; 
}
