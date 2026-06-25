package com.enginertugrul.iottemperaturemonitor.dto;


import java.time.LocalDate;

public record SensorDailyAverageDTO(LocalDate date, Double averageTemperature) {

}
