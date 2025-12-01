package com.buildenough.logisticsmanagement.controller;

import com.buildenough.logisticsmanagement.dto.WarehouseResponse;
import com.buildenough.logisticsmanagement.dto.WarehousecreateRequest;
import com.buildenough.logisticsmanagement.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // 창고 등록
    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(
            @Valid @RequestBody WarehousecreateRequest request
    ) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity.ok(response);
    }

    // 창고 전체 조회
    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        List<WarehouseResponse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    // 창고 삭제(비활성화)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateWarehouse(@PathVariable Long id) {
        warehouseService.activateWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<WarehouseResponse>> getInactiveWarehouses() {
        List<WarehouseResponse> warehouses = warehouseService.getInactiveWarehouses();
        return ResponseEntity.ok(warehouses);
    }
}
