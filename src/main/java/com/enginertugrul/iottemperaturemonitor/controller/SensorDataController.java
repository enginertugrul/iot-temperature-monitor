package com.enginertugrul.iottemperaturemonitor.controller;


import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.service.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SensorDataController {

    private final Logger LOGGER = LoggerFactory.getLogger(SensorDataController.class);
    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }



    @GetMapping("/")
    public String getHomePage(Model model) {

        List<SensorViewDTO> recentRecords = sensorDataService.getRecentTenRecords();

        model.addAttribute("recentRecords", recentRecords);
        return "index";
    }




    @PostMapping("/readings")
    public ResponseEntity<Void> receiveData(@RequestParam("celsiusValue") Double celsiusValue) {
        LOGGER.info("Received Value: {}", celsiusValue);
        sensorDataService.save(celsiusValue);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/statistics")
    public String getSensorStatistics(Model model) {
        List<SensorDailyAverageDTO> weeklyData = sensorDataService.getDailyAverageFromLastWeek();
        model.addAttribute("weeklyData", weeklyData);

        return "statistics";
    }



}
