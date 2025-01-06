package com.smartcontrol.controller;

import com.smartcontrol.dto.ScheduleDTO;
import com.smartcontrol.dto.DeviceDTO;
import com.smartcontrol.model.Schedule;
import com.smartcontrol.model.Device;
import com.smartcontrol.model.User;
import com.smartcontrol.service.ScheduleService;
import com.smartcontrol.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DeviceService deviceService;

    
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setDevice(scheduleService.getDeviceById(scheduleDTO.getDeviceId()));
        schedule.setAction(scheduleDTO.getAction());
        schedule.setScheduledTime(scheduleDTO.getScheduledTime());
        schedule.setExecuted(scheduleDTO.isExecuted());

        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(ScheduleDTO.toDto(createdSchedule));
    }

    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDevice(@PathVariable Long deviceId) {
        List<Schedule> schedules = scheduleService.getSchedulesByDevice(deviceId);
        List<ScheduleDTO> response = schedules.stream().map(ScheduleDTO::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/school")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesBySchool(@AuthenticationPrincipal User manager) {
        if (manager.getSchool() == null) {
            throw new IllegalArgumentException("소속된 학교가 없습니다.");
        }

        Long schoolId = manager.getSchool().getId();
        List<Schedule> schedules = scheduleService.getSchedulesBySchool(schoolId);

        List<ScheduleDTO> response = schedules.stream()
                .map(ScheduleDTO::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @PutMapping("/{scheduleId}/execute")
    public ResponseEntity<ScheduleDTO> updateScheduleExecution(@PathVariable Long scheduleId, @RequestParam boolean isExecuted) {
        Schedule updatedSchedule = scheduleService.updateScheduleExecution(scheduleId, isExecuted);
        return ResponseEntity.ok(ScheduleDTO.toDto(updatedSchedule));
    }

    
    @GetMapping("/school/devices")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<DeviceDTO>> getDevicesBySchool(@AuthenticationPrincipal User manager) {
        if (manager.getSchool() == null) {
            throw new IllegalArgumentException("소속된 학교가 없습니다.");
        }

        Long schoolId = manager.getSchool().getId();
        List<Device> devices = deviceService.getDevicesBySchool(schoolId);

        List<DeviceDTO> response = devices.stream()
                .map(DeviceDTO::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
