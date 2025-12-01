package com.buildenough.logisticsmanagement.dto.outbound;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class OutboundCreateRequest {

    @NotNull
    private Long warehouseId;

    // 출고 시간 안정해주면 현재 시간 적용
    private LocalDateTime outboundDate;

    private String customerName;
    private String remark;

    @NotNull
    private List<OutboundItemRequest> items;
}
