package com.task.congestionchargetaxapp.service;

import com.task.congestionchargetaxapp.entities.CongestionTaxEntity;
import com.task.congestionchargetaxapp.enums.CarType;
import com.task.congestionchargetaxapp.repository.CityTaxationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;

@Service
public class CityTaxationService {

    private final CityTaxationRepository cityTaxationRepository;

    @Autowired
    public CityTaxationService(CityTaxationRepository cityTaxationRepository) {
        this.cityTaxationRepository = cityTaxationRepository;
    }

    public double calculateCityCongestionTaxForASingleDay(List<Timestamp> carTimeStampsWithinADay, List<CongestionTaxEntity> congestionTaxRulesList, String cityName, String carType) {
        // I guess a colleague made an implementation to retrieve the date of the proposal through the sensors for one car in one day

        Map<LocalTime, Double> entryFeeMap;

        final long maximumTaxAmountPerDay = getMaximumTaxAmountPerDayByCity(cityName);

        LocalDate date = getDateFromTimeStampsList(carTimeStampsWithinADay);
        if (isExemptedFromTax(date, carType)) {
            return 0;
        }

        Collections.sort(carTimeStampsWithinADay);
        List<LocalTime> carLocalTimeList = getTimeListFromTimestampList(carTimeStampsWithinADay);

        // I mapped all the camera/sensor passes and their prices.
        entryFeeMap = mapEntryTimesToFees(carLocalTimeList, congestionTaxRulesList);
        double taxToPay = calculateTaxAmount(entryFeeMap);

        return Math.min(taxToPay, maximumTaxAmountPerDay);


    }

    private double calculateTaxAmount(Map<LocalTime, Double> entryFeeMap) {
        List<Entry<LocalTime, Double>> entries = new ArrayList<>(entryFeeMap.entrySet());
        entries.sort(Entry.comparingByKey());

        double sum = 0;
        int i = 0;

        while (i < entries.size()) {
            LocalTime startTime = entries.get(i).getKey();
            double maxInWindow = entries.get(i).getValue();

            int j = i + 1;
            while (j < entries.size() && entries.get(j).getKey().isBefore(startTime.plusMinutes(60))) {
                maxInWindow = Math.max(maxInWindow, entries.get(j).getValue());
                j++;
            }
            sum += maxInWindow;
            i = j;
        }
        return sum;

    }

    private Map<LocalTime, Double> mapEntryTimesToFees(List<LocalTime> carLocalTimeList, List<CongestionTaxEntity> congestionTaxRulesList) {
        Map<LocalTime, Double> mappedFeesByTime = new HashMap<>();
        for (LocalTime carLocalTime : carLocalTimeList) {
            for (CongestionTaxEntity congestionSingleTaxRule : congestionTaxRulesList) {
                if (carLocalTime.isAfter(congestionSingleTaxRule.getStartTime()) && carLocalTime.isBefore(congestionSingleTaxRule.getEndTime())) {
                    double amount = congestionSingleTaxRule.getAmount();
                    mappedFeesByTime.put(carLocalTime, amount);
                }
            }
        }
        return mappedFeesByTime;
    }

    private List<LocalTime> getTimeListFromTimestampList(List<Timestamp> timestampsWithDay) {
        return timestampsWithDay.stream()
                .map(timestamp -> timestamp.toLocalDateTime().toLocalTime())
                .toList();
    }

    private LocalDate getDateFromTimeStampsList(List<Timestamp> carTimeStampsWithinADay) {
        // I guess a colleague made an implementation to retrieve the date of the proposal
        // through the sensors for one car in one day
        // There is no need to check the other days in the code, just take the first one from the list. (for now)
        // TODO: add validation whether all the dates from the submitted list are the same.
        if (carTimeStampsWithinADay == null || carTimeStampsWithinADay.isEmpty()) {
            throw new IllegalArgumentException("The list of timestamps cannot be null or empty");
        }
        return carTimeStampsWithinADay.get(0).toLocalDateTime().toLocalDate();
    }

    private boolean isExemptedFromTax(LocalDate date, String carType) {
        return isWeekend(date) || isJuly(date) || isCarTaxFree(carType);

        //TODO: Add more conditions regarding public holidays
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isJuly(LocalDate date) {
        return date.getMonthValue() == 7;
    }

    // colleague implemented logic with car type
    private boolean isCarTaxFree(String carType) {
        return carType.equals(CarType.CARNONPAYINGTAX.getCarType());
    }


    private long getMaximumTaxAmountPerDayByCity(String cityName) {
        //todo: improvement - add entity validator to make city name unique and added enums for cities
        return cityTaxationRepository.findMaximumTaxAmountPerDayByCityName(cityName);
    }
}
