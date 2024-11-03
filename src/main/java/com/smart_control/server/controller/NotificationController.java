package com.smart_control.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendSensorDataNotification(String message) {
        messagingTemplate.convertAndSend("/topic/sensorData", message);
    }

    public void sendDeviceStatusNotification(String message) {
        messagingTemplate.convertAndSend("/topic/deviceStatus", message);
    }
}
