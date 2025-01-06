package com.smartcontrol.controller;

import com.smartcontrol.dto.DeviceDTO;
import com.smartcontrol.model.Device;
import com.smartcontrol.service.DeviceService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    
    @PostMapping
    public ResponseEntity<Device> registerDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        Device device = deviceService.registerDevice(
                deviceDTO.getName(),
                Device.Status.valueOf(deviceDTO.getStatus().toUpperCase()),
                Device.SensorType.valueOf(deviceDTO.getSensorType().toUpperCase()),
                deviceDTO.getSchoolId()
        );
        return ResponseEntity.ok(device);
    }

    
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    
    @GetMapping("/school/{schoolId}")
    public ResponseEntity<List<Device>> getDevicesBySchool(@PathVariable Long schoolId) {
        return ResponseEntity.ok(deviceService.getDevicesBySchool(schoolId));
    }

    
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
    }

    
    @PutMapping("/{deviceId}/status")
    public ResponseEntity<Device> updateDeviceStatus(
            @PathVariable Long deviceId,
            @RequestParam String status) {
        Device updatedDevice = deviceService.updateDeviceStatus(
                deviceId, Device.Status.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(updatedDevice);
    }

    
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long deviceId) {
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}
