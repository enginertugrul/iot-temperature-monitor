package com.enginertugrul.iottemperaturemonitor.dto.sensor;

import com.enginertugrul.iottemperaturemonitor.entity.sensor.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorForm {

    @NotNull
    private SensorType type = SensorType.TEMPERATURE;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String district;

    @NotBlank
    @Size(max = 100)
    private String homeLocation;
}