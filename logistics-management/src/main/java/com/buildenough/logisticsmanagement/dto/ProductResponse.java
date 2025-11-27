package com.buildenough.logisticsmanagement.dto;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String unit;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.unit = product.getUnit();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }
}
