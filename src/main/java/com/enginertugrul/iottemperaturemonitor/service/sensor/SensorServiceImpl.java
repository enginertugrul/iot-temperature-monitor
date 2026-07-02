package com.enginertugrul.iottemperaturemonitor.service.sensor;

import com.enginertugrul.iottemperaturemonitor.dto.sensor.CreatedSensorDTO;
import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorForm;
import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorListItemDTO;
import com.enginertugrul.iottemperaturemonitor.entity.sensor.Sensor;
import com.enginertugrul.iottemperaturemonitor.entity.user.AppUser;
import com.enginertugrul.iottemperaturemonitor.repository.SensorRepository;
import com.enginertugrul.iottemperaturemonitor.repository.AppUserRepository;
import com.enginertugrul.iottemperaturemonitor.security.ingestion.GeneratedSensorIngestionToken;
import com.enginertugrul.iottemperaturemonitor.security.ingestion.SensorIngestionTokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final AppUserRepository appUserRepository;
    private final SensorIngestionTokenGenerator sensorIngestionTokenGenerator;

    public SensorServiceImpl(SensorRepository sensorRepository, AppUserRepository appUserRepository, SensorIngestionTokenGenerator sensorIngestionTokenGenerator) {
        this.sensorRepository = sensorRepository;
        this.appUserRepository = appUserRepository;
        this.sensorIngestionTokenGenerator = sensorIngestionTokenGenerator;
    }

    @Override
    @Transactional
    public CreatedSensorDTO createSensor(Long ownerId, SensorForm sensorForm) {
        AppUser owner = appUserRepository.findById(ownerId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        String requestedName = sensorForm.getName().trim();

        if (sensorRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, requestedName)) {
            throw new IllegalArgumentException("You already have a sensor with this name");
        }

        Sensor sensor = new Sensor(
                owner,
                sensorForm.getType(),
                requestedName,
                sensorForm.getCity(),
                sensorForm.getDistrict(),
                sensorForm.getHomeLocation(),
                sensorForm.getTimezone()
        );

        GeneratedSensorIngestionToken generatedToken = sensorIngestionTokenGenerator.generate();
        sensor.assignIngestionTokenHash(generatedToken.tokenHash());

        Sensor savedSensor = sensorRepository.save(sensor);

        return new CreatedSensorDTO(savedSensor.getId() ,
                savedSensor.getName(),
                generatedToken.rawToken());

    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorListItemDTO> getSensorsForUser(Long ownerId) {
        return sensorRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(this::toListItem)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Sensor getSensorForUser(Long sensorId, Long ownerId) {
        return sensorRepository.findByIdAndOwnerId(sensorId, ownerId)
                .orElseThrow(() -> new NoSuchElementException("Sensor not found"));
    }

    private SensorListItemDTO toListItem(Sensor sensor) {
        return new SensorListItemDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.getType(),
                sensor.getCity(),
                sensor.getDistrict(),
                sensor.getHomeLocation(),
                sensor.getTimezone(),
                sensor.isActive()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public String getDefaultTimezoneForUser(Long ownerId) {
        return appUserRepository.findById(ownerId)
                .orElseThrow(() -> new NoSuchElementException("User not found"))
                .getPreferredTimezone();
    }



}