package com.smartcontrol.repository;

import com.smartcontrol.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findByEmailAndTimestampAfter(String email, LocalDateTime timestamp); 
}
