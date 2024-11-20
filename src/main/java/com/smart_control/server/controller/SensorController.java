package com.smart_control.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.SensorData;
import com.smart_control.server.service.SensorDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 1. add sensor data
// 2. get all sensors' data
// 3. get sensor data by device id
// 4. get sensor data by type

@RestController
@RequestMapping("/api/sensors")
public class SensorController {
    @Autowired
    private SensorDataService sensorDataService;

    @PostMapping("/data")
    public ResponseEntity<SensorData> addSensorData(@RequestBody SensorData sensorData) {
        SensorData savedData = sensorDataService.saveSensorData(sensorData);
        return ResponseEntity.ok().body(savedData);
    }
    
    @GetMapping("/data")
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        return ResponseEntity.ok(sensorDataService.getAllSensorData());
    }
    
    @GetMapping("/data/device/{id}")
    public ResponseEntity<List<SensorData>> getSensorDataByType(@PathVariable Long id, Authentication authentication) {
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        return ResponseEntity.ok(sensorDataService.getSensorDataByDeviceId(id, schoolId));
    }
    
    @GetMapping("/data/type/{type}")
    public ResponseEntity<List<SensorData>> getSensorDataByType(@PathVariable String type, Authentication authentication) {
        Long schoolId = getSchoolIdFromAuthentication(authentication);
        return ResponseEntity.ok(sensorDataService.getSensorDataByType(type, schoolId));
    }

    private Long getSchoolIdFromAuthentication(Authentication authentication) {
        return (Long) authentication.getDetails();
    }
    
}
