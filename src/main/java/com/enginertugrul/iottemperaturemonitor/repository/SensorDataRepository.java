package com.enginertugrul.iottemperaturemonitor.repository;

import com.enginertugrul.iottemperaturemonitor.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

//    @NativeQuery("SELECT * FROM sensor_data ORDER BY sensor_data.timestamp DESC LIMIT 1")
    Optional<SensorData> findFirstByOrderByTimestampDesc();



    @Modifying
    @Query("DELETE FROM SensorData s WHERE s.timestamp < :cutoffTimeStamp ")
    void deleteOlderThan(Instant cutoffTimeStamp);



    @Query("SELECT s FROM SensorData s ORDER BY timestamp DESC LIMIT 10")
    List<SensorData> getRecentTenValues();


}
