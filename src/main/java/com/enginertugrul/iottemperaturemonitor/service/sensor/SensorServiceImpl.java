package com.enginertugrul.iottemperaturemonitor.service.sensor;

import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorForm;
import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorListItemDTO;
import com.enginertugrul.iottemperaturemonitor.entity.sensor.Sensor;
import com.enginertugrul.iottemperaturemonitor.entity.user.AppUser;
import com.enginertugrul.iottemperaturemonitor.repository.SensorRepository;
import com.enginertugrul.iottemperaturemonitor.repository.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final AppUserRepository appUserRepository;

    public SensorServiceImpl(SensorRepository sensorRepository, AppUserRepository appUserRepository) {
        this.sensorRepository = sensorRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public Sensor createSensor(Long ownerId, SensorForm sensorForm) {
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
                owner.getPreferredTimezone()
        );

        return sensorRepository.save(sensor);
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
}