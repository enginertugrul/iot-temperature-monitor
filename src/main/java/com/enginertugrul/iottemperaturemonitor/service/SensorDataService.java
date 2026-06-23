package com.enginertugrul.iottemperaturemonitor.service;

import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SensorDataService {


    void saveData(Double sensorValue);

    SensorViewDTO getSensorData();

    List<SensorViewDTO> getRecentTenRecords();

}
