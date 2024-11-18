package com.smart_control.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_control.server.model.DeviceControl;

@Repository
public interface DeviceControlRepository extends JpaRepository<DeviceControl, Long> {
    DeviceControl findTopByDeviceNameOrderByTimestampDesc(String deviceName);

    List<DeviceControl> findBySchool_Id(Long schoolId);

    DeviceControl findByDeviceNameAndSchool_Id(String deviceName, Long schoolId);
}
