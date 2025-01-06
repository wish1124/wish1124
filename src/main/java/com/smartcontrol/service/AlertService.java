package com.smartcontrol.service;

import com.smartcontrol.model.Alert;
import com.smartcontrol.model.Alert.AlertStatus;
import com.smartcontrol.model.Device;
import com.smartcontrol.repository.AlertRepository;
import com.smartcontrol.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    
    public Alert createAlert(Long deviceId, String message) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));

        Alert alert = new Alert();
        alert.setDevice(device);
        alert.setMessage(message);
        alert.setStatus(AlertStatus.NEW);
        alert.setTimestamp(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    
    public List<Alert> getAlertsByDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));

        return alertRepository.findByDevice(device);
    }

    
    public List<Alert> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    
    public Alert updateAlertStatus(Long alertId, AlertStatus status) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        alert.setStatus(status);
        return alertRepository.save(alert);
    }
}
