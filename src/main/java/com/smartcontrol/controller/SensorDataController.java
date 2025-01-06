package com.smartcontrol.controller;

import com.smartcontrol.dto.SensorDataDTO;
import com.smartcontrol.dto.SensorStatisticsDTO;
import com.smartcontrol.dto.SensorSummaryDTO;
import com.smartcontrol.model.Device;
import com.smartcontrol.model.SensorData;
import com.smartcontrol.model.User;
import com.smartcontrol.repository.DeviceRepository;
import com.smartcontrol.service.SensorDataService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensors")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private DeviceRepository deviceRepository;

    
    @PostMapping
    public ResponseEntity<SensorData> saveSensorData(@Valid @RequestBody SensorDataDTO sensorDataDTO) {
        SensorData sensorData = sensorDataService.saveSensorData(
                sensorDataDTO.getDeviceId(),
                Device.SensorType.valueOf(sensorDataDTO.getSensorType().name()),
                sensorDataDTO.getValue(),
                sensorDataDTO.getTimestamp()
        );
        return ResponseEntity.ok(sensorData);
    }

    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<SensorData>> getSensorDataByDevice(
            @PathVariable Long deviceId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);

        List<SensorData> data = sensorDataService.getSensorDataByDevice(deviceId, start, end);
        return ResponseEntity.ok(data);
    }

    
    @GetMapping("/type/{sensorType}")
    public ResponseEntity<List<SensorData>> getSensorDataByType(
            @PathVariable String sensorType,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);

        List<SensorData> data = sensorDataService.getSensorDataByType(
                Device.SensorType.valueOf(sensorType.toUpperCase()), start, end);
        return ResponseEntity.ok(data);
    }

    
    @DeleteMapping("/delete-old")
    public ResponseEntity<Void> deleteOldSensorData(@RequestParam String beforeDate) {
        LocalDateTime before = LocalDateTime.parse(beforeDate, DateTimeFormatter.ISO_DATE_TIME);
        sensorDataService.deleteOldSensorData(before);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Device>> getDevicesBySchool(@AuthenticationPrincipal User user) {
        List<Device> devices = deviceRepository.findBySchoolId(user.getSchool().getId());
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSensorSummary() {
        Map<String, Object> summary = sensorDataService.getSensorSummary();
        return ResponseEntity.ok(summary);
    }

    
    @GetMapping("/{deviceId}/data")
    public ResponseEntity<List<SensorDataDTO>> getHistoricalData(@PathVariable Long deviceId) {
        List<SensorDataDTO> data = sensorDataService.getHistoricalData(deviceId);
        return ResponseEntity.ok(data);
    }

    
    @PostMapping("/{deviceId}/live")
    public ResponseEntity<Void> receiveLiveData(@PathVariable Long deviceId, @RequestBody SensorDataDTO sensorData) {
        sensorDataService.processLiveData(deviceId, sensorData);
        return ResponseEntity.ok().build();
    }
}
