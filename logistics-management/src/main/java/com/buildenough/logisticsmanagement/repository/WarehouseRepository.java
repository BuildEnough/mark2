package com.buildenough.logisticsmanagement.repository;

import com.buildenough.logisticsmanagement.domain.Warehouse;
import com.buildenough.logisticsmanagement.domain.WarehouseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByCode(String code);
    boolean existsByCode(String code);

    // ACTIVE 창고만 조회
    List<Warehouse> findByStatus(WarehouseStatus status);
}
