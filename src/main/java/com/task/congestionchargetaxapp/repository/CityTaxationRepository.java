package com.task.congestionchargetaxapp.repository;

import com.task.congestionchargetaxapp.entities.CityTaxationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityTaxationRepository extends JpaRepository<CityTaxationEntity, Long> {
    CityTaxationEntity findMaximumTaxAmountPerDayByCityName(String cityName);
}
