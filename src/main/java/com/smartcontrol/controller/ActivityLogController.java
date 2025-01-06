package com.smartcontrol.controller;

import com.smartcontrol.model.ActivityLog;
import com.smartcontrol.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    
    @GetMapping
    public ResponseEntity<List<ActivityLog>> getAllLogs() {
        return ResponseEntity.ok(activityLogService.getAllLogs());
    }

    
    @GetMapping("/user/{email}")
    public ResponseEntity<List<ActivityLog>> getLogsByUser(@PathVariable String email) {
        return ResponseEntity.ok(activityLogService.getLogsByUser(email));
    }

    
    @GetMapping("/action/{action}")
    public ResponseEntity<List<ActivityLog>> getLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(activityLogService.getLogsByAction(action));
    }
}
