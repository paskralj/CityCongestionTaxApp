package com.task.congestionchargetaxapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class CityTaxationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cityName;

    @OneToMany
    @JoinColumn(name = "city_taxation_id")
    private List<CongestionTaxEntity> congestionTaxEntity;

    private long maximumTaxAmountPerDay;
}
