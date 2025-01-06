package com.smartcontrol.service;

import com.smartcontrol.model.ActivityLog;
import com.smartcontrol.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    
    public ActivityLog createLog(String userEmail, String action, String ipAddress) {
        ActivityLog log = new ActivityLog();
        log.setUserEmail(userEmail);
        log.setAction(action);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());
        return activityLogRepository.save(log);
    }

    
    public List<ActivityLog> getLogsByUser(String email) {
        return activityLogRepository.findByUserEmail(email);
    }

    
    public List<ActivityLog> getLogsByAction(String action) {
        return activityLogRepository.findByAction(action);
    }

    
    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll();
    }
}
