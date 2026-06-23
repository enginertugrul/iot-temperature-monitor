package com.enginertugrul.iottemperaturemonitor.scheduler;

import com.enginertugrul.iottemperaturemonitor.repository.SensorDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class DataRetentionScheduler {


    private final SensorDataRepository sensorDataRepository;

    public DataRetentionScheduler(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }



    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeOldSensorData() {

        short daysToKeepSensorData = 14;
        Instant cutoffDate = Instant.now().minus(daysToKeepSensorData, ChronoUnit.DAYS);

        sensorDataRepository.deleteOlderThan(cutoffDate);

    }


}
