package com.buildenough.logisticsmanagement.repository.stock;

import com.buildenough.logisticsmanagement.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByWarehouse_IdAndProduct_id(Long warehouseId, Long productId);
}
