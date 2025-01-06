package com.smartcontrol.controller;

import com.smartcontrol.model.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SensorDataWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastSensorData(SensorData sensorData) {
        messagingTemplate.convertAndSend("/topic/sensor-data", sensorData);
    }
}
