package com.enginertugrul.iottemperaturemonitor.service.sensorreading;

import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorHourlyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.entity.reading.SensorReading;
import com.enginertugrul.iottemperaturemonitor.entity.sensor.Sensor;
import com.enginertugrul.iottemperaturemonitor.entity.sensor.SensorType;
import com.enginertugrul.iottemperaturemonitor.repository.SensorReadingRepository;
import com.enginertugrul.iottemperaturemonitor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {


    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingServiceImpl(SensorReadingRepository sensorReadingRepository, SensorRepository sensorRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    @Transactional
    public void saveTemperatureReading(Long sensorId, Double celsiusValue) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new NoSuchElementException("Sensor not found"));

        if (!sensor.isActive()) {
            throw new IllegalArgumentException("Sensor is not active");
        }

        if (sensor.getType() != SensorType.TEMPERATURE) {
            throw new IllegalArgumentException("Sensor is not a temperature sensor");
        }

        SensorReading reading = SensorReading.temperature(sensor, celsiusValue, Instant.now());
        sensorReadingRepository.save(reading);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorViewDTO> getRecentTenRecords(Long sensorId, Long ownerId) {
        Sensor sensor = getOwnedSensor(sensorId, ownerId);

        return sensorReadingRepository.findTop10BySensorIdAndSensorOwnerIdOrderByRecordedAtDesc(sensorId, ownerId)
                .stream()
                .map(reading -> toViewDTO(reading, sensor))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDailyAverageDTO> getDailyAverageFromLastWeek(Long sensorId, Long ownerId) {
        Sensor sensor = getOwnedSensor(sensorId, ownerId);
        ZoneId zoneId = ZoneId.of(sensor.getTimezone());
        LocalDate today = LocalDate.now(zoneId);
        Instant untilDate = today.minusDays(6).atStartOfDay(zoneId).toInstant();

        List<SensorDailyAverageDTO> dbResults =
                sensorReadingRepository.findDailyAverageValuesSince(sensorId, untilDate, sensor.getTimezone());

        Map<LocalDate, Double> dataMap = dbResults.stream()
                .collect(Collectors.toMap(SensorDailyAverageDTO::date, SensorDailyAverageDTO::averageTemperature));

        List<SensorDailyAverageDTO> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            result.add(new SensorDailyAverageDTO(date, dataMap.getOrDefault(date, 0.0)));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorHourlyAverageDTO> getHourlyAverageForDate(Long sensorId, Long ownerId, LocalDate date) {
        Sensor sensor = getOwnedSensor(sensorId, ownerId);
        ZoneId zoneId = ZoneId.of(sensor.getTimezone());

        Instant startOfDay = date.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant();

        List<SensorHourlyAverageDTO> dbResults =
                sensorReadingRepository.findHourlyAverageValuesForDate(
                        sensorId,
                        startOfDay,
                        endOfDay,
                        sensor.getTimezone()
                );

        Map<Short, Double> dataMap = dbResults.stream()
                .collect(Collectors.toMap(SensorHourlyAverageDTO::hour, SensorHourlyAverageDTO::average));

        List<SensorHourlyAverageDTO> result = new ArrayList<>();

        for (short hour = 0; hour <= 23; hour++) {
            result.add(new SensorHourlyAverageDTO(hour, dataMap.get(hour)));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDate getTodayForSensor(Long sensorId, Long ownerId) {
        Sensor sensor = getOwnedSensor(sensorId, ownerId);
        return LocalDate.now(ZoneId.of(sensor.getTimezone()));
    }

    private Sensor getOwnedSensor(Long sensorId, Long ownerId) {
        return sensorRepository.findByIdAndOwnerId(sensorId, ownerId)
                .orElseThrow(() -> new NoSuchElementException("Sensor not found"));
    }

    private SensorViewDTO toViewDTO(SensorReading reading, Sensor sensor) {
        return new SensorViewDTO(
                sensor.getHomeLocation(),
                reading.getNumericValue(),
                reading.getRecordedAt().atZone(ZoneId.of(sensor.getTimezone()))
        );
    }
}
