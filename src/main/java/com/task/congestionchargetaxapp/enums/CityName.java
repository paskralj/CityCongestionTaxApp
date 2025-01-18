package com.task.congestionchargetaxapp.enums;

import lombok.Getter;

@Getter
public enum CityName {
    GOTHENBURG("Gothenburg");

    private final String cityName;

    CityName(String cityName) {
        this.cityName = cityName;
    }
}
