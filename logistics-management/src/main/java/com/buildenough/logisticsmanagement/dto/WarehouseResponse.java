package com.buildenough.logisticsmanagement.dto;

import com.buildenough.logisticsmanagement.domain.Warehouse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WarehouseResponse {
    private Long id;
    private String code;
    private String name;
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WarehouseResponse(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.code = warehouse.getCode();
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.description = warehouse.getDescription();
        this.createdAt = warehouse.getCreatedAt();
        this.updatedAt = warehouse.getUpdatedAt();
    }
}
