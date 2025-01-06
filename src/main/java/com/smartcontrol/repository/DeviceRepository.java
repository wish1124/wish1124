package com.smartcontrol.repository;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.School;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findBySchool(School school); 

    @Query("SELECT d FROM Device d WHERE d.school.id = :schoolId")
    List<Device> findBySchoolId(@Param("schoolId") Long schoolId);
}
