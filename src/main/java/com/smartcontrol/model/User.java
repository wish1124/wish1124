package com.smartcontrol.model;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; 

    @Column(nullable = false)
    private String password; 

    @Column(nullable = false)
    private String name; 

    private String phone; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; 

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school; 

    public enum Role {
        ADMIN,
        MANAGER,
        USER
    }

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    public UserDetails toUserDetails() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
        return new org.springframework.security.core.userdetails.User(this.getEmail(), this.getPassword(), authorities);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

}
