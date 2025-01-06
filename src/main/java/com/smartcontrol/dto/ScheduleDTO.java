package com.smartcontrol.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.smartcontrol.model.Schedule;

@Data
public class ScheduleDTO {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String action;
    private LocalDateTime scheduledTime;
    private boolean isExecuted;

    
    public static ScheduleDTO toDto(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDeviceId(schedule.getDevice().getId());
        dto.setDeviceName(schedule.getDevice().getName());
        dto.setAction(schedule.getAction());
        dto.setScheduledTime(schedule.getScheduledTime());
        dto.setExecuted(schedule.isExecuted());
        return dto;
    }
}
