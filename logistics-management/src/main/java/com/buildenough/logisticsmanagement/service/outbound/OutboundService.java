package com.buildenough.logisticsmanagement.service.outbound;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.Warehouse;
import com.buildenough.logisticsmanagement.domain.outbound.Outbound;
import com.buildenough.logisticsmanagement.domain.outbound.OutboundItem;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundItemRequest;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundResponse;
import com.buildenough.logisticsmanagement.repository.ProductRepository;
import com.buildenough.logisticsmanagement.repository.WarehouseRepository;
import com.buildenough.logisticsmanagement.repository.outbound.OutboundRepository;
import com.buildenough.logisticsmanagement.service.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OutboundService {
    private final OutboundRepository outboundRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;

    public OutboundService(OutboundRepository outboundRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, StockService stockService) {
        this.outboundRepository = outboundRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
    }

    @Transactional
    public OutboundResponse createOutbound(OutboundCreateRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다. id=" + request.getWarehouseId()));

        LocalDateTime outboundDate = request.getOutboundDate();
        if (outboundDate == null) {
            outboundDate = LocalDateTime.now();
        }

        Outbound outbound = new Outbound(
                outboundDate,
                warehouse,
                request.getCustomerName(),
                request.getRemark()
        );

        // 각 아이템 처리, 재고 감소
        for (OutboundItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + itemRequest.getProductId()));

            OutboundItem item = new OutboundItem(
                    product,
                    itemRequest.getQuantity()
            );

            outbound.addItem(item);

            // 재고 감소
            stockService.decreaseStock(
                    warehouse.getId(),
                    product.getId(),
                    itemRequest.getQuantity()
            );
        }

        Outbound saved = outboundRepository.save(outbound);
        return new OutboundResponse(saved);
    }
}
