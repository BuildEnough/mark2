package com.buildenough.logisticsmanagement.controller.controller;

import com.buildenough.logisticsmanagement.dto.outbound.OutboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundResponse;
import com.buildenough.logisticsmanagement.service.outbound.OutboundService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/outbounds")
public class OutboundController {

    private final OutboundService outboundService;

    public OutboundController(OutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @PostMapping
    public ResponseEntity<OutboundResponse> createOutbound(
            @Valid @RequestBody OutboundCreateRequest request
    ) {
        OutboundResponse response = outboundService.createOutbound(request);
        return ResponseEntity.ok(response);
    }
}
