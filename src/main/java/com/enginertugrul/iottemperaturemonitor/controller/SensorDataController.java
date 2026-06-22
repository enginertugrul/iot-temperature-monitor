package com.enginertugrul.iottemperaturemonitor.controller;


import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.service.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SensorDataController {


    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }


    @PostMapping("/data")
    public ResponseEntity<Void> receiveData(@RequestParam("value") Integer value) {
        System.out.println("Received Value: " + value);
        sensorDataService.saveData(value);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/received")
    public String getLatestSensorData(Model model) {
        SensorViewDTO latestData = sensorDataService.getSensorData();
        model.addAttribute("sensor", latestData);
        return "received";
    }



}
