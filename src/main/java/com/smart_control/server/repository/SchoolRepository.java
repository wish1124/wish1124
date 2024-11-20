package com.smart_control.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_control.server.model.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
    void deleteByName(String name);
}
