package com.smartcontrol.controller;

import com.smartcontrol.dto.SensorDataDTO;
import com.smartcontrol.service.SensorDataService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    private final SensorDataService sensorService;

    public WebSocketMessageController(SensorDataService sensorService) {
        this.sensorService = sensorService;
    }

    
    @MessageMapping("/sensors/{deviceId}")
    @SendTo("/topic/sensors/{deviceId}")
    public SensorDataDTO handleSensorMessage(SensorDataDTO sensorDataDTO) {
        
        return sensorService.processSensorData(sensorDataDTO);
    }
}
