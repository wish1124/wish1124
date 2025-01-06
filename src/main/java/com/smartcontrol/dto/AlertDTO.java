package com.smartcontrol.dto;

import com.smartcontrol.model.Alert;

import lombok.Data;

@Data
public class AlertDTO {

    private Long id;
    private String message;
    private String status;
    private String timestamp;
    private Long deviceId;

    public static AlertDTO fromEntity(Alert alert) {
        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setMessage(alert.getMessage());
        dto.setStatus(alert.getStatus().name());
        dto.setTimestamp(alert.getTimestamp().toString());
        dto.setDeviceId(alert.getDevice().getId());
        return dto;
    }
}
