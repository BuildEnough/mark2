package com.buildenough.logisticsmanagement.service.stock;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.Warehouse;
import com.buildenough.logisticsmanagement.domain.stock.Stock;
import com.buildenough.logisticsmanagement.dto.stock.StockResponse;
import com.buildenough.logisticsmanagement.repository.ProductRepository;
import com.buildenough.logisticsmanagement.repository.WarehouseRepository;
import com.buildenough.logisticsmanagement.repository.stock.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(StockResponse::new)
                .toList();
    }

    // 창고 + 상품 기준 재고 엔티티 가져오기, 없으면 생
    @Transactional
    public Stock getOrCreateStock(Long warehouseId, Long productId) {
        return stockRepository.findByWarehouse_IdAndProduct_id(warehouseId, productId)
                .orElseGet(() -> {
                    Warehouse warehouse = warehouseRepository.findById(warehouseId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니. id=" +  warehouseId));
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" +  productId));

                    Stock newStock= new Stock(warehouse, product, 0L);
                    return stockRepository.save(newStock);
                });
    }

    @Transactional
    public void increaseStock(Long warehouseId, Long productId, Long quantity) {
        Stock stock = getOrCreateStock(warehouseId, productId);
        stock.increase(quantity);
    }

    @Transactional
    public void decreaseStock(Long warehouseId, Long productId, Long quantity) {
        Stock stock = getOrCreateStock(warehouseId, productId);
        stock.decrease(quantity);
    }

}
