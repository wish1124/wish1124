package com.smart_control.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.smart_control.server.controller.NotificationController;
import com.smart_control.server.model.Schedule;
import com.smart_control.server.repository.ScheduleRepository;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DeviceControlService deviceControlService;

    @Autowired
    private NotificationController notificationController;

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Scheduled(fixedRate = 2000)
    public void executeScheduleTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> dueSchedules = scheduleRepository.findByScheduledTimeBefore(now);

        for (Schedule schedule : dueSchedules) {

            deviceControlService.updateDeviceStatus(schedule.getDevice().getDeviceName(), schedule.isStatus(), schedule.getSchool().getId());

            notificationController.sendDeviceStatusNotification(schedule.getDevice().getDeviceName() + "의 예약된 상태가 변경되었습니다. " + (schedule.isStatus() ? "ON" : "OFF"));

            scheduleRepository.delete(schedule);
        }
    }
}
