package com.buildenough.logisticsmanagement.service.inbound;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.Warehouse;
import com.buildenough.logisticsmanagement.domain.inbound.Inbound;
import com.buildenough.logisticsmanagement.domain.inbound.InboundItem;
import com.buildenough.logisticsmanagement.dto.inbound.InboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.inbound.InboundItemRequest;
import com.buildenough.logisticsmanagement.dto.inbound.InboundResponse;
import com.buildenough.logisticsmanagement.repository.ProductRepository;
import com.buildenough.logisticsmanagement.repository.WarehouseRepository;
import com.buildenough.logisticsmanagement.repository.inbound.InboundRepository;
import com.buildenough.logisticsmanagement.repository.stock.StockRepository;
import com.buildenough.logisticsmanagement.service.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InboundService {
    private final StockService stockService;
    private InboundRepository inboundRepository;
    private WarehouseRepository warehouseRepository;
    private ProductRepository productRepository;
    private StockRepository stockRepository;

    public InboundService(InboundRepository inboundRepository,
                          WarehouseRepository warehouseRepository,
                          ProductRepository productRepository,
                          StockRepository stockRepository, StockService stockService) {
        this.inboundRepository = inboundRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public InboundResponse createInbound(InboundCreateRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다. id=" + request.getWarehouseId()));

        LocalDateTime inboundDate = request.getInboundDate();
        if (inboundDate == null) {
            inboundDate = LocalDateTime.now();
        }

        Inbound inbound = new Inbound(
                warehouse,
                inboundDate,
                request.getRemark()
        );

        // 상세 아이템 처리, 재고 증가
        for (InboundItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + itemRequest.getProductId()));

            InboundItem item = new InboundItem(
                    product,
                    itemRequest.getQuantity()
            );

            inbound.addItem(item);

            // 재고 증가
            stockService.increaseStock(
                    warehouse.getId(),
                    product.getId(),
                    itemRequest.getQuantity()
            );
        }

        Inbound saved = inboundRepository.save(inbound);
        return new InboundResponse(saved);

    }

}
