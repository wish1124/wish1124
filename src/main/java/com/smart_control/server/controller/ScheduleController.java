package com.smart_control.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.Schedule;
import com.smart_control.server.service.ScheduleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 1. add schedule

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService schedulesService;

    @PostMapping("/add")
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule) {
        Schedule savedSchedule = schedulesService.addSchedule(schedule);
        return ResponseEntity.ok(savedSchedule);
    }
    
}
