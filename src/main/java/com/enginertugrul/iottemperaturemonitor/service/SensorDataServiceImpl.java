package com.enginertugrul.iottemperaturemonitor.service;


import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.entity.SensorData;
import com.enginertugrul.iottemperaturemonitor.repository.SensorDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService {


    private final SensorDataRepository sensorDataRepository;

    public SensorDataServiceImpl(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @Override
    @Transactional
    public void save(Double sensorValue) {
        String locationOfSensor = "Hall";
        SensorData sensorData = new SensorData (locationOfSensor, sensorValue, Instant.now());
        sensorDataRepository.save(sensorData);
    }




    @Override
    @Transactional(readOnly = true)
    public List<SensorViewDTO> getRecentTenRecords() {
        return sensorDataRepository.findRecentTenValues().stream().map(sensorData -> new SensorViewDTO(
                sensorData.getLocationOfSensor(),
                sensorData.getCelsiusValue(),
                sensorData.getRecordedAt().atZone(ZoneId.of("Europe/Istanbul"))
        )).toList();
    }




    @Override
    @Transactional(readOnly = true)
    public List<SensorDailyAverageDTO> getDailyAverageFromLastWeek() {


        String timezone = "Europe/Istanbul";

        ZoneId zoneId = ZoneId.of(timezone);
        LocalDate today = LocalDate.now(zoneId);
        Instant untilDate = today.minusDays(6).atStartOfDay(zoneId).toInstant();


        List<SensorDailyAverageDTO> dbResults = sensorDataRepository.findDailyAverageTemperaturesSince(untilDate, timezone);

        // Convert results to a Map for fast O(1) lookups
        Map<LocalDate, Double> dataMap = dbResults.stream()
                .collect(Collectors.toMap(SensorDailyAverageDTO::date, SensorDailyAverageDTO::averageTemperature));

        List<SensorDailyAverageDTO> completeWeeklyData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Double avgTemp = dataMap.getOrDefault(date, 0.0);
            completeWeeklyData.add(new SensorDailyAverageDTO(date, avgTemp));
        }

        return completeWeeklyData;
    }










}
