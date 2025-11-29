package com.buildenough.logisticsmanagement.dto.inbound;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class InboundCreateRequest {

    @NotNull
    private Long warehouseId;

    // 입고날짜가 언제인지
    private LocalDateTime inboundDate;

    // 비고
    private String remark;

    @NotNull
    private List<InboundItemRequest> items;
}
