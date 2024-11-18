package com.smart_control.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_control.server.model.User;
import com.smart_control.server.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authService;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        authService.register(user);
        token = authService.login("testuser", "testpassword");
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"testuser\", \"password\":\"testpassword\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testAddSensorData() throws Exception {
        mockMvc.perform(post("/api/sensors/data")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"type\":\"temperature\", \"value\":25.0}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllSensorData() throws Exception {
        mockMvc.perform(get("/api/sensors/data")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateDeviceStatus() throws Exception {
        mockMvc.perform(post("/api/devices/control")
            .header("Authorization", "Bearer " + token)
            .param("deviceName", "LED")
            .param("status", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(true));
    }
}
