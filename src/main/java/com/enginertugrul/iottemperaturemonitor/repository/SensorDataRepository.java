package com.enginertugrul.iottemperaturemonitor.repository;

import com.enginertugrul.iottemperaturemonitor.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

//    @NativeQuery("SELECT * FROM sensor_data ORDER BY sensor_data.timestamp DESC LIMIT 1")
    Optional<SensorData> findFirstByOrderByTimestampDesc();



}
