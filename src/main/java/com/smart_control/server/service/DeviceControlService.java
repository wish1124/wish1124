package com.smart_control.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_control.server.controller.NotificationController;
import com.smart_control.server.model.DeviceControl;
import com.smart_control.server.repository.DeviceControlRepository;

@Service
public class DeviceControlService {
    @Autowired
    private DeviceControlRepository deviceControlRepository;

    @Autowired
    private NotificationController notificationController;

    public DeviceControl updateDeviceStatus(String deviceName, boolean status) {
        DeviceControl control = new DeviceControl();
        control.setDeviceName(deviceName);
        control.setStatus(status);
        control.setTimestamp(LocalDateTime.now());

        DeviceControl savedControl = deviceControlRepository.save(control);

        notificationController.sendDeviceStatusNotification(deviceName + "의 상태가 변경되었습니다.");

        return savedControl;
    }

    public List<DeviceControl> getAllDeviceStatus() {
        return deviceControlRepository.findAll();
    }

    public DeviceControl getDeviceStatus(String deviceName) {
        return deviceControlRepository.findTopByDeviceNameOrderByTimestampDesc(deviceName);
    }
}
