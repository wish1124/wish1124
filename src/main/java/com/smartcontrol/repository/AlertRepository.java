package com.smartcontrol.repository;

import com.smartcontrol.model.Alert;
import com.smartcontrol.model.Device;
import com.smartcontrol.model.Alert.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByDevice(Device device);
    List<Alert> findByStatus(AlertStatus status);
}
