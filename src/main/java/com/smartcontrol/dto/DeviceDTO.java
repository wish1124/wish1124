package com.smartcontrol.dto;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.School;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDTO {
    private Long id;

    @NotBlank(message = "기기 이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "기기 상태는 필수 항목입니다.")
    private String status;

    @NotNull(message = "센서 타입은 필수 항목입니다.")
    private String sensorType;

    @NotNull(message = "학교 ID는 필수 항목입니다.")
    private Long schoolId;

    
    public static DeviceDTO toDto(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setStatus(device.getStatus().name());
        dto.setSensorType(device.getSensorType().name());
        dto.setSchoolId(device.getSchool() != null ? device.getSchool().getId() : null);
        return dto;
    }

    
    public static Device fromDto(DeviceDTO dto, School school) {
        Device device = new Device();
        device.setId(dto.getId());
        device.setName(dto.getName());
        device.setStatus(Device.Status.valueOf(dto.getStatus().toUpperCase()));
        device.setSensorType(Device.SensorType.valueOf(dto.getSensorType().toUpperCase()));
        device.setSchool(school);
        return device;
    }
}
