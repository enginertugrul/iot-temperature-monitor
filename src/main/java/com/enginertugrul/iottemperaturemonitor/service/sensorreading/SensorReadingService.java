package com.enginertugrul.iottemperaturemonitor.service.sensorreading;

import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorHourlyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;

import java.time.LocalDate;
import java.util.List;

public interface SensorReadingService {


    void saveTemperatureReading(Long sensorId, Double celsiusValue);

    List<SensorViewDTO> getRecentTenRecords(Long sensorId, Long ownerId);

    List<SensorDailyAverageDTO> getDailyAverageFromLastWeek(Long sensorId, Long ownerId);

    List<SensorHourlyAverageDTO> getHourlyAverageForDate(Long sensorId, Long ownerId, LocalDate date);

    LocalDate getTodayForSensor(Long sensorId, Long ownerId);


}
