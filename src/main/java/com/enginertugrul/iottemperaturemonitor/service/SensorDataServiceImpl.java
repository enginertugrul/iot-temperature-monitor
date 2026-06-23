package com.enginertugrul.iottemperaturemonitor.service;


import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.entity.SensorData;
import com.enginertugrul.iottemperaturemonitor.repository.SensorDataRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SensorDataServiceImpl implements SensorDataService {


    private final SensorDataRepository sensorDataRepository;

    public SensorDataServiceImpl(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @Override
    public void saveData(Double sensorValue) {
        String locationOfSensor = "Hall";
        SensorData sensorData = new SensorData (locationOfSensor, sensorValue, Instant.now());
        sensorDataRepository.save(sensorData);
    }

    @Override
    public SensorViewDTO getSensorData() {
        return sensorDataRepository.findFirstByOrderByTimestampDesc()
                .map(sensorData -> new SensorViewDTO(
                        sensorData.getLocationOfSensor(),
                        sensorData.getCelsiusValue(),
                        sensorData.getTimestamp()
                ))
                .orElseThrow();
    }
}
