package com.smartcontrol.service;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.School;
import com.smartcontrol.repository.DeviceRepository;
import com.smartcontrol.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    
    public Device registerDevice(String name, Device.Status status, Device.SensorType sensorType, Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));

        Device device = new Device();
        device.setName(name);
        device.setStatus(status);
        device.setSensorType(sensorType);
        device.setSchool(school);

        return deviceRepository.save(device);
    }

    
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    
    public List<Device> getDevicesBySchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));
        return deviceRepository.findBySchool(school);
    }

    
    public Device getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));
    }

    
    public Device updateDeviceStatus(Long deviceId, Device.Status status) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));
        device.setStatus(status);
        return deviceRepository.save(device);
    }

    
    public void deleteDevice(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));
        deviceRepository.delete(device);
    }
}
