package com.enginertugrul.iottemperaturemonitor.service.sensor;

import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorForm;
import com.enginertugrul.iottemperaturemonitor.dto.sensor.SensorListItemDTO;
import com.enginertugrul.iottemperaturemonitor.entity.sensor.Sensor;

import java.util.List;

public interface SensorService {

    Sensor createSensor(Long ownerId, SensorForm sensorForm);

    List<SensorListItemDTO> getSensorsForUser(Long ownerId);

    Sensor getSensorForUser(Long sensorId, Long ownerId);

    String getDefaultTimezoneForUser(Long ownerId);
}