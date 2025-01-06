package com.smartcontrol.repository;

import com.smartcontrol.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByUserEmail(String email); 

    List<ActivityLog> findByAction(String action); 
}
