package com.smart_control.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_control.server.model.DeviceControl;

@Repository
public interface DeviceControlRepository extends JpaRepository<DeviceControl, Long> {
    DeviceControl findTopByDeviceNameOrderByTimestampDesc(String deviceName);
}
