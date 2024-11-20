package com.smart_control.server.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart_control.server.model.School;
import com.smart_control.server.model.User;
import com.smart_control.server.repository.DeviceControlRepository;
import com.smart_control.server.repository.ScheduleRepository;
import com.smart_control.server.repository.SchoolRepository;
import com.smart_control.server.repository.SensorDataRepository;
import com.smart_control.server.repository.UserRepository;
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

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceControlRepository deviceControlRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    private String tokenSchoolA;
    private String tokenSchoolB;
    private Long schoolAId;
    private Long schoolBId;

    @BeforeEach
    void setUp() throws Exception {
        // userRepository.deleteAll();
        // scheduleRepository.deleteAll();
        // sensorDataRepository.deleteAll();
        // deviceControlRepository.deleteAll();
        // schoolRepository.deleteAll();

        // School schoolA = new School("School 1");
        // schoolA = schoolRepository.save(schoolA);
        // schoolAId = schoolA.getId();

        // School schoolB = new School("School 2");
        // schoolB = schoolRepository.save(schoolB);
        // schoolBId = schoolB.getId();

        // User userA = new User("testuser1", "testpassword", schoolA, "Alice");
        // authService.register(userA);
        tokenSchoolA = authService.login("testuser1", "testpassword");

        // User userB = new User("testuser2", "testpassword", schoolA, "Bob");
        // authService.register(userB);
        // tokenSchoolB = authService.login("testuser2", "testpassword");
    }

    @AfterEach
    void end() throws Exception {
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"testuser1\", \"password\":\"testpassword\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testAddDevice() throws Exception {
        mockMvc.perform(post("/api/devices/control")
            .header("Authorization", "Bearer " + tokenSchoolA)
            .param("deviceName", "Air Conditional")
            .param("status", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testUpdateDeviceStatus() throws Exception {
        mockMvc.perform(put("/api/devices/control")
            .header("Authorization", "Bearer " + tokenSchoolA)
            .param("deviceName", "Computer")
            .param("status", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testAddSensorData() throws Exception {
        mockMvc.perform(post("/api/sensors/data")
            .header("Authorization", "Bearer " + tokenSchoolA)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"type\":\"temperature\", \"value\":25.0}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetAllSensorData() throws Exception {
        mockMvc.perform(get("/api/sensors/data")
            .header("Authorization", "Bearer " + tokenSchoolA))
            .andExpect(status().isOk());
    }
}
