package com.buildenough.logisticsmanagement.dto.stock;

import com.buildenough.logisticsmanagement.domain.stock.Stock;
import lombok.Getter;

@Getter
public class StockResponse {
    private Long id;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;

    private Long productId;
    private String productCode;
    private String productName;

    private Long quantity;

    public StockResponse(Stock stock) {
        this.id = stock.getId();
        this.warehouseId = stock.getWarehouse().getId();
        this.warehouseCode = stock.getWarehouse().getCode();
        this.warehouseName = stock.getWarehouse().getName();

        this.productId = stock.getProduct().getId();
        this.productCode = stock.getProduct().getCode();
        this.productName = stock.getProduct().getName();

        this.quantity = stock.getQuantity();
    }

}
