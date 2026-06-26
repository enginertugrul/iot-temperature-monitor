package com.enginertugrul.iottemperaturemonitor.controller;


import com.enginertugrul.iottemperaturemonitor.dto.SensorDailyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorHourlyAverageDTO;
import com.enginertugrul.iottemperaturemonitor.dto.SensorViewDTO;
import com.enginertugrul.iottemperaturemonitor.service.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
    public ResponseEntity<Void> receiveTemperatureData(@RequestParam("celsiusValue") Double celsiusValue) {
        LOGGER.info("Received Value: {}", celsiusValue);
        sensorDataService.save(celsiusValue);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/statistics")
    public String getSensorStatistics(Model model) {

        LocalDate today = LocalDate.now(ZoneId.of("Europe/Istanbul"));

        List<SensorDailyAverageDTO> weeklyData = sensorDataService.getDailyAverageFromLastWeek();
        List<SensorHourlyAverageDTO> hourlyData = sensorDataService.getHourlyAverageForDate(today);


        model.addAttribute("weeklyData", weeklyData);
        model.addAttribute("hourlyData", hourlyData);
        model.addAttribute("today", today.toString());

        return "statistics";
    }




    @GetMapping("/api/statistics/hourly")
    @ResponseBody
    public ResponseEntity<List<SensorHourlyAverageDTO>> getHourlyDataForDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<SensorHourlyAverageDTO> hourlyData = sensorDataService.getHourlyAverageForDate(date);
        return ResponseEntity.ok(hourlyData);
    }



}