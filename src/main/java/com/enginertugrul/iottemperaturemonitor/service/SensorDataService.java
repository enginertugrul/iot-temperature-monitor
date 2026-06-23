package com.enginertugrul.iottemperaturemonitor.service;

import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import org.springframework.stereotype.Service;

@Service
public interface SensorDataService {


    void saveData(Double sensorValue);

    SensorViewDTO getSensorData();


}
