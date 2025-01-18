package com.task.congestionchargetaxapp.enums;

import lombok.Getter;

@Getter
public enum CarType {
    // This is only temporary, I think that my colleague has already implemented this logic and knows
    // from the license plates whether the car pays tax
    CARPAYINGTAX("CarPayingTax"),
    CARNONPAYINGTAX("CarNonPayingTax");

    private final String carType;

    CarType(String carType) {
        this.carType = carType;
    }
}
