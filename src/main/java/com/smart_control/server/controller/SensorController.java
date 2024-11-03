package com.smart_control.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.SensorData;
import com.smart_control.server.service.SensorDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    
    @GetMapping("/data/{type}")
    public ResponseEntity<List<SensorData>> getSensorDataByType(@PathVariable String type) {
        return ResponseEntity.ok(sensorDataService.getSensorDataByType(type));
    }
    
}
