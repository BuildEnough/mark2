package com.buildenough.logisticsmanagement.repository;

import com.buildenough.logisticsmanagement.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code); // 찾기
    boolean existsByCode(String code); // 유무 확인
}
