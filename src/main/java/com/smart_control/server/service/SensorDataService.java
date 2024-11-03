package com.smart_control.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_control.server.controller.NotificationController;
import com.smart_control.server.model.SensorData;
import com.smart_control.server.repository.SensorDataRepository;

@Service
public class SensorDataService {
    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private NotificationController notificationController;

    public SensorData saveSensorData(SensorData data) {
        data.setTimestamp(LocalDateTime.now());
        SensorData savedData = sensorDataRepository.save(data);

        notificationController.sendSensorDataNotification("새로운 센서 데이터가 추가되었습니다");

        return savedData;
    }

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public List<SensorData> getSensorDataByType(String type) {
        return sensorDataRepository.findByType(type);
    }
}
