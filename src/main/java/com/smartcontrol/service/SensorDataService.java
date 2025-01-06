package com.smartcontrol.service;

import com.smartcontrol.controller.SensorDataWebSocketController;
import com.smartcontrol.dto.SensorDataDTO;
import com.smartcontrol.dto.SensorStatisticsDTO;
import com.smartcontrol.dto.SensorSummaryDTO;
import com.smartcontrol.model.Device;
import com.smartcontrol.model.SensorData;
import com.smartcontrol.repository.DeviceRepository;
import com.smartcontrol.repository.SensorDataRepository;
import com.smartcontrol.websocket.DeviceWebSocketHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    @Autowired
    public SensorDataRepository sensorDataRepository;

    @Autowired
    public DeviceRepository deviceRepository;

    @Autowired
    private SensorDataWebSocketController webSocketController;

    @Autowired
    private DeviceWebSocketHandler deviceWebSocketHandler;


    
    public SensorData saveSensorData(Long deviceId, Device.SensorType sensorType, double value, LocalDateTime timestamp) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));

        SensorData sensorData = new SensorData();
        sensorData.setDevice(device);
        sensorData.setSensorType(sensorType);
        sensorData.setValue(value);
        sensorData.setTimestamp(timestamp);

        SensorData savedData = sensorDataRepository.save(sensorData);

        
        webSocketController.broadcastSensorData(savedData);

        return savedData;
    }

    
    public List<SensorData> getSensorDataByDevice(Long deviceId, LocalDateTime startDate, LocalDateTime endDate) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다."));
        return sensorDataRepository.findByDeviceAndTimestampBetween(device, startDate, endDate);
    }

    
    public List<SensorData> getSensorDataByType(Device.SensorType sensorType, LocalDateTime startDate, LocalDateTime endDate) {
        return sensorDataRepository.findBySensorTypeAndTimestampBetween(sensorType, startDate, endDate);
    }

    
    public void deleteOldSensorData(LocalDateTime beforeDate) {
        List<SensorData> oldData = sensorDataRepository.findAll().stream()
                .filter(data -> data.getTimestamp().isBefore(beforeDate))
                .toList();
        sensorDataRepository.deleteAll(oldData);
    }

    
    public Device getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 기기 ID입니다."));
    }

    
    public List<SensorStatisticsDTO> getStatisticsByDevice(Long deviceId) {
        List<Object[]> statistics = sensorDataRepository.findStatisticsByDeviceId(deviceId);

        
        return statistics.stream().map(stat -> {
            SensorStatisticsDTO dto = new SensorStatisticsDTO();
            dto.setType((String) stat[0]);
            dto.setMax((Double) stat[1]);
            dto.setMin((Double) stat[2]);
            dto.setAvg((Double) stat[3]);
            return dto;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getSensorSummary() {
        long totalSensors = sensorDataRepository.count();
        double averageValue = sensorDataRepository.calculateAverageSensorValue();
        double maxValue = sensorDataRepository.findMaxSensorValue();
        double minValue = sensorDataRepository.findMinSensorValue();
    
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSensors", totalSensors > 0 ? totalSensors : 0);
        summary.put("averageValue", totalSensors > 0 ? averageValue : 0);
        summary.put("maxValue", totalSensors > 0 ? maxValue : 0);
        summary.put("minValue", totalSensors > 0 ? minValue : 0);
    
        return summary;
    }

    public List<SensorDataDTO> getHistoricalData(Long deviceId) {
        return sensorDataRepository.findByDeviceId(deviceId).stream()
                .map(SensorDataDTO::toDto)
                .collect(Collectors.toList());
    }

    public void processLiveData(Long deviceId, SensorDataDTO sensorDataDTO) {
        
        SensorData sensorData = new SensorData();
        Device device = new Device();
        device.setId(deviceId); 

        sensorData.setDevice(device);
        sensorData.setSensorType(sensorDataDTO.getSensorType());
        sensorData.setValue(sensorDataDTO.getValue());
        sensorData.setTimestamp(sensorDataDTO.getTimestamp());
        sensorDataRepository.save(sensorData);

        
        deviceWebSocketHandler.sendMessageToDevice(
            "/topic/sensors/" + deviceId.toString(),
            sensorDataDTO.toJson() 
        );
    }
    
    public SensorDataDTO processSensorData(SensorDataDTO sensorDataDTO) {
        Optional<Device> deviceOpt = deviceRepository.findById(sensorDataDTO.getDeviceId());
        if (deviceOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid device ID");
        }

        Device device = deviceOpt.get();
        SensorData sensorData = new SensorData();
        sensorData.setDevice(device);
        sensorData.setSensorType(sensorDataDTO.getSensorType());
        sensorData.setValue(sensorDataDTO.getValue());
        sensorData.setTimestamp(sensorDataDTO.getTimestamp());

        SensorData savedData = sensorDataRepository.save(sensorData);

        return SensorDataDTO.toDto(savedData);
    }
}
