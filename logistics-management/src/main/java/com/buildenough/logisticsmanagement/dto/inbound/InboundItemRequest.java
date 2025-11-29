package com.buildenough.logisticsmanagement.dto.inbound;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InboundItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Long quantity;

}
