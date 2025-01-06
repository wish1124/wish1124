package com.smartcontrol.dto;

import lombok.Data;

@Data
public class SensorStatisticsDTO {
    private String type; 
    private double max;  
    private double min;  
    private double avg;  
}
