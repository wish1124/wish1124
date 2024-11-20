package com.smart_control.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_control.server.model.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByType(String type);

    List<SensorData> findByDevice_Id(Long id);
}
