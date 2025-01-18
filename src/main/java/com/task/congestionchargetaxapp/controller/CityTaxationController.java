package com.task.congestionchargetaxapp.controller;

import com.task.congestionchargetaxapp.entities.CongestionTaxEntity;
import com.task.congestionchargetaxapp.service.CityTaxationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tax")
public class CityTaxationController {

    private final CityTaxationService cityTaxationService;

    @Autowired
    public CityTaxationController(CityTaxationService cityTaxationService) {
        this.cityTaxationService = cityTaxationService;
    }


    // /tax/calculate?cityName=Gothenburg&carType=CarPayingTax
    @GetMapping("/calculate")
    public ResponseEntity<String> calculateCityCongestionTaxForASingleDayByCar(@RequestParam String cityName, @RequestParam String carType) {

        // From the car registration/carType and city, you can get carTimeStampsWithinADay and congestionTaxRulesList.
        // We are limited by time - I would add this logic as an improvement in Services

        // I added "simulated" data to save time and focus on the logic (copy-paste from UTs)


        List<CongestionTaxEntity> congestionTaxRulesList = new ArrayList<>();
        congestionTaxRulesList.add(createTaxEntity("00:00:00", "05:59:59", 0));
        congestionTaxRulesList.add(createTaxEntity("06:00:00", "06:29:59", 8));
        congestionTaxRulesList.add(createTaxEntity("06:30:00", "06:59:59", 13));
        congestionTaxRulesList.add(createTaxEntity("07:00:00", "07:59:59", 18));
        congestionTaxRulesList.add(createTaxEntity("08:00:00", "08:29:59", 13));
        congestionTaxRulesList.add(createTaxEntity("08:30:00", "14:59:59", 8));
        congestionTaxRulesList.add(createTaxEntity("15:00:00", "15:29:59", 13));
        congestionTaxRulesList.add(createTaxEntity("15:30:00", "16:59:59", 18));
        congestionTaxRulesList.add(createTaxEntity("17:00:00", "17:59:59", 13));
        congestionTaxRulesList.add(createTaxEntity("18:00:00", "18:29:59", 8));
        congestionTaxRulesList.add(createTaxEntity("18:30:00", "23:59:59", 0));

        List<Timestamp> carTimeStampsWithinADay1 = new ArrayList<>();
        carTimeStampsWithinADay1.add(Timestamp.valueOf("2013-02-07 06:23:27"));
        carTimeStampsWithinADay1.add(Timestamp.valueOf("2013-02-07 15:27:00"));


        double result = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay1, congestionTaxRulesList, cityName, carType);
        
        return ResponseEntity.ok(String.valueOf(result));

    }
    private CongestionTaxEntity createTaxEntity(String start, String end, double amount) {
        CongestionTaxEntity entity = new CongestionTaxEntity();
        entity.setStartTime(LocalTime.parse(start));
        entity.setEndTime(LocalTime.parse(end));
        entity.setAmount(amount);
        return entity;
    }

}
