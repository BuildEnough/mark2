package com.buildenough.logisticsmanagement.repository;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code); // 찾기
    boolean existsByCode(String code); // 유무 확인

    List<Product> findByStatus(ProductStatus status); // 상태별 조회용
}
