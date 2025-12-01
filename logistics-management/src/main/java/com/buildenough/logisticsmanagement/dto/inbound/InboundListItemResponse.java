package com.buildenough.logisticsmanagement.dto.inbound;

import com.buildenough.logisticsmanagement.domain.inbound.Inbound;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InboundListItemResponse {

    private Long id;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private LocalDateTime inboundDate;
    private String remark;
    private int itemCount;
    private long totalQuantity;

    public InboundListItemResponse(Inbound inbound) {
        this.id = inbound.getId();
        this.warehouseId = inbound.getWarehouse().getId();
        this.warehouseCode = inbound.getWarehouse().getCode();
        this.warehouseName = inbound.getWarehouse().getName();
        this.inboundDate = inbound.getInboundDate();
        this.remark = inbound.getRemark();
        this.itemCount = inbound.getItems().size();
        this.totalQuantity = inbound.getItems().stream()
                .mapToLong(i -> i.getQuantity())
                .sum();
    }
}
