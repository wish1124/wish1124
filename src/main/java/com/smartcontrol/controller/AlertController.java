package com.smartcontrol.controller;

import com.smartcontrol.dto.AlertDTO;
import com.smartcontrol.model.Alert;
import com.smartcontrol.model.Alert.AlertStatus;
import com.smartcontrol.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    
    @PostMapping
    public ResponseEntity<AlertDTO> createAlert(@RequestParam Long deviceId, @RequestParam String message) {
        Alert alert = alertService.createAlert(deviceId, message);
        return ResponseEntity.ok(AlertDTO.fromEntity(alert));
    }

    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<AlertDTO>> getAlertsByDevice(@PathVariable Long deviceId) {
        List<Alert> alerts = alertService.getAlertsByDevice(deviceId);
        List<AlertDTO> response = alerts.stream().map(AlertDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AlertDTO>> getAlertsByStatus(@PathVariable String status) {
        AlertStatus alertStatus = AlertStatus.valueOf(status.toUpperCase());
        List<Alert> alerts = alertService.getAlertsByStatus(alertStatus);
        List<AlertDTO> response = alerts.stream().map(AlertDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @PutMapping("/{alertId}/status")
    public ResponseEntity<AlertDTO> updateAlertStatus(@PathVariable Long alertId, @RequestParam String status) {
        AlertStatus alertStatus = AlertStatus.valueOf(status.toUpperCase());
        Alert updatedAlert = alertService.updateAlertStatus(alertId, alertStatus);
        return ResponseEntity.ok(AlertDTO.fromEntity(updatedAlert));
    }
}
