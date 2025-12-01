package com.buildenough.logisticsmanagement.dto.outbound;

import com.buildenough.logisticsmanagement.domain.outbound.Outbound;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OutboundResponse {

    private Long id;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private LocalDateTime outboundDate;
    private String customerName;
    private String remark;

    public OutboundResponse(Outbound outbound) {
        this.id = outbound.getId();
        this.warehouseId = outbound.getWarehouse().getId();
        this.warehouseCode = outbound.getWarehouse().getCode();
        this.warehouseName = outbound.getWarehouse().getName();
        this.outboundDate = outbound.getOutboundDate();
        this.customerName = outbound.getCustomerName();
        this.remark = outbound.getRemark();
    }
}
