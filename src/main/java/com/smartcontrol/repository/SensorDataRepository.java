package com.smartcontrol.repository;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByDevice(Device device);

    List<SensorData> findByDeviceAndTimestampBetween(Device device, LocalDateTime startDate, LocalDateTime endDate);

    List<SensorData> findBySensorTypeAndTimestampBetween(Device.SensorType sensorType, LocalDateTime startDate, LocalDateTime endDate);
    
    List<SensorData> findByDeviceId(Long deviceId); 
    

    @Query("SELECT s.sensorType, MAX(s.value), MIN(s.value), AVG(s.value) " +
       "FROM SensorData s WHERE s.device.id = :deviceId GROUP BY s.sensorType")
    List<Object[]> findStatisticsByDeviceId(Long deviceId); 

    @Query("SELECT AVG(s.value) FROM SensorData s WHERE s.sensorType = :sensorType")
    Double calculateAverageValueByType(@Param("sensorType") Device.SensorType sensorType);

    @Query("SELECT COUNT(s) FROM SensorData s WHERE s.sensorType = :sensorType AND s.value > :threshold")
    long countAlertsByThreshold(@Param("sensorType") Device.SensorType sensorType, @Param("threshold") double threshold);

    @Query("SELECT COALESCE(AVG(s.value), 0) FROM SensorData s")
    double calculateAverageSensorValue();

    @Query("SELECT COALESCE(MAX(s.value), 0) FROM SensorData s")
    double findMaxSensorValue();

    @Query("SELECT COALESCE(MIN(s.value), 0) FROM SensorData s")
    double findMinSensorValue();

}
