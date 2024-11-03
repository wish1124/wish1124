package com.smart_control.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.DeviceControl;
import com.smart_control.server.model.SensorData;
import com.smart_control.server.service.DeviceControlService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    @Autowired
    private DeviceControlService deviceControlService;

    @PostMapping("/control")
    public ResponseEntity<DeviceControl> controlDevice(@RequestParam String deviceName, @RequestParam boolean status) {
        DeviceControl control = deviceControlService.updateDeviceStatus(deviceName, status);
        return ResponseEntity.ok().body(control);
    }
    
    @GetMapping("/control")
    public ResponseEntity<List<DeviceControl>> getAllDeviceStatus() {
        return ResponseEntity.ok(deviceControlService.getAllDeviceStatus());
    }
    
    @GetMapping("/control/{deviceName}")
    public ResponseEntity<DeviceControl> getDeviceStatus(@PathVariable String deviceName) {
        return ResponseEntity.ok(deviceControlService.getDeviceStatus(deviceName));
    }
}
