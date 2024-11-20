package com.smart_control.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.DeviceControl;
import com.smart_control.server.model.SensorData;
import com.smart_control.server.service.DeviceControlService;

// 1. add device
// 2. update device - status
// 3. fetch all devices' status
// 4. fetch device's status by device name

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    @Autowired
    private DeviceControlService deviceControlService;

    @PostMapping("/control")
    public ResponseEntity<DeviceControl> addControlDevice(@RequestParam String deviceName, @RequestParam boolean status, 
                Authentication authentication) {
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        DeviceControl control = deviceControlService.addDevice(deviceName, status, schoolId);
        return ResponseEntity.ok().body(control);
    }

    @PutMapping("/control")
    public ResponseEntity<DeviceControl> updateControlDevice(@RequestParam String deviceName, @RequestParam boolean status, 
                Authentication authentication) {
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        DeviceControl control = deviceControlService.updateDeviceStatus(deviceName, status, schoolId);
        return ResponseEntity.ok().body(control);
    }
    
    @GetMapping("/status-debug")
    public ResponseEntity<List<DeviceControl>> getAllDeviceStatus() {
        return ResponseEntity.ok(deviceControlService.getAllDeviceStatus());
    }
    
    @GetMapping("/status")
    public ResponseEntity<List<DeviceControl>> getAllDeviceStatus(Authentication authentication) {
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        return ResponseEntity.ok(deviceControlService.getAllDeviceStatusBySchool(schoolId));
    }
    
    @GetMapping("/status-debug/{deviceName}")
    public ResponseEntity<DeviceControl> getDeviceStatus(@PathVariable String deviceName) {
        // Todo: 없을 경우에 404코드 전달 필요
        return ResponseEntity.ok(deviceControlService.getDeviceStatus(deviceName));
    }

    @GetMapping("/status/{deviceName}")
    public ResponseEntity<DeviceControl> getDeviceStatus(@PathVariable String deviceName, Authentication authentication) {
        // Todo: 없을 경우에 404코드 전달 필요
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        return ResponseEntity.ok(deviceControlService.getDeviceStatusBySchool(deviceName, schoolId));
    }

    private Long getSchoolIdFromAuthentication(Authentication authentication) {
        return (Long) authentication.getDetails();
    }
}
