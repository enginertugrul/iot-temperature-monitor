package com.enginertugrul.iottemperaturemonitor.service;

import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorHourlyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface SensorDataService {


    void save(Double sensorValue);


    List<SensorViewDTO> getRecentTenRecords();

    List<SensorDailyAverageDTO> getDailyAverageFromLastWeek();

    List<SensorHourlyAverageDTO> getHourlyAverageForDate(LocalDate date);


}
