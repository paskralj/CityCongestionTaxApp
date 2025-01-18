package com.task.congestionchargetaxapp.service;

import com.task.congestionchargetaxapp.entities.CityTaxationEntity;
import com.task.congestionchargetaxapp.entities.CongestionTaxEntity;
import com.task.congestionchargetaxapp.enums.CarType;
import com.task.congestionchargetaxapp.repository.CityTaxationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CityTaxationServiceTest {

    @Mock
    private CityTaxationRepository cityTaxationRepository;

    @InjectMocks
    private CityTaxationService cityTaxationService;

    private List<Timestamp> carTimeStampsWithinADay1;
    private List<Timestamp> carTimeStampsWithinADay2;
    private List<Timestamp> carTimeStampsWithinADay3;
    private List<Timestamp> carTimeStampsWithinADay4;
    private CityTaxationEntity cityTaxationEntity;
    private List<CongestionTaxEntity> congestionTaxRulesList;

    @BeforeEach
    void setUp() {
        carTimeStampsWithinADay1 = new ArrayList<>();
        carTimeStampsWithinADay1.add(Timestamp.valueOf("2013-02-07 06:23:27"));
        carTimeStampsWithinADay1.add(Timestamp.valueOf("2013-02-07 15:27:00"));

        carTimeStampsWithinADay2 = new ArrayList<>();
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 06:20:27"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 06:27:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 14:35:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 15:29:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 15:47:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 16:01:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 16:48:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 17:49:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 18:29:00"));
        carTimeStampsWithinADay2.add(Timestamp.valueOf("2013-02-08 18:35:00"));

        congestionTaxRulesList = new ArrayList<>();
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

        carTimeStampsWithinADay3 = new ArrayList<>();
        carTimeStampsWithinADay3.add(Timestamp.valueOf("2013-02-08 06:20:27"));
        carTimeStampsWithinADay3.add(Timestamp.valueOf("2013-02-08 06:27:00"));

        carTimeStampsWithinADay4 = new ArrayList<>();
        carTimeStampsWithinADay4.add(Timestamp.valueOf("2013-07-07 06:23:27"));
        carTimeStampsWithinADay4.add(Timestamp.valueOf("2013-07-07 15:27:00"));

        cityTaxationEntity = new CityTaxationEntity();
        cityTaxationEntity.setMaximumTaxAmountPerDay(60L);
    }

    @Test
    void testCalculateCityCongestionTaxForASingleDay(){
        when(cityTaxationRepository.findMaximumTaxAmountPerDayByCityName(anyString())).thenReturn(cityTaxationEntity);

        double result1 = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay1, congestionTaxRulesList, "Gothenburg", CarType.CARPAYINGTAX.getCarType());
        assertEquals(21L, result1);

        double result2 = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay2, congestionTaxRulesList, "Gothenburg", CarType.CARPAYINGTAX.getCarType());
        assertEquals(60L, result2);

        double result3 = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay3, congestionTaxRulesList, "Gothenburg", CarType.CARPAYINGTAX.getCarType());
        assertEquals(8L, result3);

        // Test with exempted from tax - NONPAYINGCAR
        double result4 = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay3, congestionTaxRulesList, "Gothenburg", CarType.CARNONPAYINGTAX.getCarType());
        assertEquals(0L, result4);

        // Test with exempted from tax - 7th month
        double result5 = cityTaxationService.calculateCityCongestionTaxForASingleDay(carTimeStampsWithinADay4, congestionTaxRulesList, "Gothenburg", CarType.CARPAYINGTAX.getCarType());
        assertEquals(0L, result5);

        // TODO: Add more tests (Eg. Odd-numbered pass test)

    }

    private CongestionTaxEntity createTaxEntity(String start, String end, double amount) {
        CongestionTaxEntity entity = new CongestionTaxEntity();
        entity.setStartTime(LocalTime.parse(start));
        entity.setEndTime(LocalTime.parse(end));
        entity.setAmount(amount);
        return entity;
    }
}
