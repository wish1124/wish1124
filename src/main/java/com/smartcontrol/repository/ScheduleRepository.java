package com.smartcontrol.repository;

import com.smartcontrol.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDeviceId(Long deviceId); 

    List<Schedule> findByScheduledTimeBeforeAndIsExecutedFalse(LocalDateTime time); 

    @Query("SELECT s FROM Schedule s WHERE s.device.school.id = :schoolId")
    List<Schedule> findBySchoolId(Long schoolId); 
}
