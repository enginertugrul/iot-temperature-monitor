package com.enginertugrul.iottemperaturemonitor.repository;

import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {


    @Modifying
    @Query("DELETE FROM SensorData s WHERE s.recordedAt < :cutoffTimeStamp ")
    void deleteOlderThan(Instant cutoffTimeStamp);



    @Query("SELECT s FROM SensorData s ORDER BY s.recordedAt DESC LIMIT 10")
    List<SensorData> findRecentTenValues();



    @NativeQuery("SELECT DATE(timestamp AT TIME ZONE :timezone) AS day , AVG(temperature_value) AS avg_temperature FROM sensor_data WHERE timestamp >= :untilDate  GROUP BY day ORDER BY day;")
    List<SensorDailyAverageDTO> findDailyAverageTemperaturesSince(Instant untilDate , String timezone);





}
