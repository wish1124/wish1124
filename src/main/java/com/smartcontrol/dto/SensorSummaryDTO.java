package com.smartcontrol.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.smartcontrol.model.Schedule;

@Data
public class SensorSummaryDTO {
    private double avgTemp;
    private long alertCount;
    private long deviceCount;
}
