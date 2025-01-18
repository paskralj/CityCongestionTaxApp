package com.task.congestionchargetaxapp.repository;

import com.task.congestionchargetaxapp.entities.CongestionTaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongestionTaxRepository extends JpaRepository<CongestionTaxEntity, Long> {
}
