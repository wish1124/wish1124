package com.smartcontrol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartcontrol.model.Device;
import com.smartcontrol.model.SensorData;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorDataDTO {

    private Long deviceId;
    private Device.SensorType sensorType; 
    private double value;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    
    public static SensorDataDTO toDto(SensorData sensorData) {
        SensorDataDTO dto = new SensorDataDTO();
        dto.setDeviceId(sensorData.getDevice().getId());
        dto.setSensorType(sensorData.getSensorType());
        dto.setValue(sensorData.getValue());
        dto.setTimestamp(sensorData.getTimestamp());
        return dto;
    }

    
    public static SensorData toEntity(SensorDataDTO dto, Device device) {
        SensorData entity = new SensorData();
        entity.setDevice(device);
        entity.setSensorType(dto.getSensorType());
        entity.setValue(dto.getValue());
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }

    
    public String toJson() {
        return String.format(
            "{\"deviceId\":%d,\"sensorType\":\"%s\",\"value\":%.2f,\"timestamp\":\"%s\"}",
            deviceId, sensorType.name(), value, timestamp.toString()
        );
    }
}
