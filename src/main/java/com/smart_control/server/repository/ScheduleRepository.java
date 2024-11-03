package com.smart_control.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_control.server.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByScheduleTimeBefore(LocalDateTime now);
}
