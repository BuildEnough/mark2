package com.buildenough.logisticsmanagement.service;

import com.buildenough.logisticsmanagement.domain.Warehouse;
import com.buildenough.logisticsmanagement.domain.WarehouseStatus;
import com.buildenough.logisticsmanagement.dto.WarehouseResponse;
import com.buildenough.logisticsmanagement.dto.WarehousecreateRequest;
import com.buildenough.logisticsmanagement.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // 창고 저장
    @Transactional
    public WarehouseResponse createWarehouse(WarehousecreateRequest request) {
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("이미 사용 중인 창고 코드입니다: " + request.getCode());
        }

        Warehouse warehouse = new Warehouse(
                request.getCode(),
                request.getName(),
                request.getLocation(),
                request.getDescription()
        );

        Warehouse saved = warehouseRepository.save(warehouse);
        return new WarehouseResponse(saved);
    }

    // 창고 조회
    @Transactional
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findByStatus(WarehouseStatus.ACTIVE)
                .stream()
                .map(WarehouseResponse::new)
                .toList();
    }

    // 창고 삭제(비활성화)
    @Transactional
    public void deleteWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다. id=" + warehouseId));
        warehouse.deactivate();
    }

}
