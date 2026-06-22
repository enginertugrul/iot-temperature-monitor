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
    public void saveData(int sensorValue) {
        Double doubleValue = (double) sensorValue;  // Currently I'm simulating the sensor values with dummy data. Going to be replaced with real data.
        String locationOfSensor = "Hall";
        SensorData sensorData = new SensorData (locationOfSensor, doubleValue, Instant.now());
        sensorDataRepository.save(sensorData);
    }

    @Override
    public SensorViewDTO getSensorData() {
        return sensorDataRepository.findFirstByOrderByTimestampDesc()
                .map(sensorData -> new SensorViewDTO(
                        sensorData.getLocationOfSensor(),
                        sensorData.getTemperatureValue(),
                        sensorData.getTimestamp()
                ))
                .orElseThrow();
    }
}
