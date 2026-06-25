package com.enginertugrul.iottemperaturemonitor.dto;

import java.time.ZonedDateTime;

public record SensorViewDTO(String locationOfSensor, Double temperatureValue, ZonedDateTime timestamp) {

}
