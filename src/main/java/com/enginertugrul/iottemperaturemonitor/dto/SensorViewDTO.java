package com.enginertugrul.iottemperaturemonitor.dto;

import java.time.Instant;

public record SensorViewDTO(String locationOfSensor, Double temperatureValue, Instant timestamp) {

}
