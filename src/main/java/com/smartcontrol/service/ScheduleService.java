package com.smartcontrol.service;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.Schedule;
import com.smartcontrol.repository.DeviceRepository;
import com.smartcontrol.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    
    public Device getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("기기를 찾을 수 없습니다. ID: " + deviceId));
    }

    public List<Schedule> getSchedulesBySchool(Long schoolId) {
        return scheduleRepository.findBySchoolId(schoolId);
    }


    
    public Schedule createSchedule(Schedule schedule) {
        if (!deviceRepository.existsById(schedule.getDevice().getId())) {
            throw new IllegalArgumentException("유효하지 않은 기기 ID입니다.");
        }
        schedule.setExecuted(false); 
        return scheduleRepository.save(schedule);
    }

    
    public List<Schedule> getSchedulesByDevice(Long deviceId) {
        return scheduleRepository.findByDeviceId(deviceId);
    }

    
    public Schedule updateScheduleExecution(Long scheduleId, boolean isExecuted) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("스케줄을 찾을 수 없습니다."));
        schedule.setExecuted(isExecuted);
        return scheduleRepository.save(schedule);
    }

    
    @Scheduled(fixedRate = 60000) 
    public void executeScheduledTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> pendingSchedules = scheduleRepository.findByScheduledTimeBeforeAndIsExecutedFalse(now);
        for (Schedule schedule : pendingSchedules) {
            executeAction(schedule);
            schedule.setExecuted(true);
            scheduleRepository.save(schedule);
        }
    }

    
    private void executeAction(Schedule schedule) {
        System.out.println("Executing action: " + schedule.getAction() + " for device ID: " + schedule.getDevice().getId());
        
    }
}
