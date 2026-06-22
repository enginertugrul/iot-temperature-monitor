package com.enginertugrul.iottemperaturemonitor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_of_sensor", nullable = false)
    private String locationOfSensor;

    @Column(name = "temperature_value", nullable = false)
    private Double temperatureValue;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    public SensorData(String locationOfSensor, Double temperatureValue, Instant timestamp) {
        this.locationOfSensor = locationOfSensor;
        this.temperatureValue = temperatureValue;
        this.timestamp = timestamp;
    }
}
