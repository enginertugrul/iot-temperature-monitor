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
    private Double celsiusValue;

    @Column(name = "timestamp", nullable = false)
    private Instant recordedAt;

    public SensorData(String locationOfSensor, Double celsiusValue, Instant recordedAt) {
        this.locationOfSensor = locationOfSensor;
        this.celsiusValue = celsiusValue;
        this.recordedAt = recordedAt;
    }





}
