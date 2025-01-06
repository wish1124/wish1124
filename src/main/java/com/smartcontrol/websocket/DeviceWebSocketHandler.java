package com.smartcontrol.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> deviceSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String deviceId = session.getUri().getPath().split("/")[3];
        deviceSessions.put(deviceId, session);
        System.out.println("WebSocket connected for device: " + deviceId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
            throws Exception {
        String deviceId = session.getUri().getPath().split("/")[3];
        deviceSessions.remove(deviceId);
        System.out.println("WebSocket disconnected for device: " + deviceId);
    }

    public void sendMessageToDevice(String deviceId, String message) {
        WebSocketSession session = deviceSessions.get(deviceId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
