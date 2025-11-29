package com.buildenough.logisticsmanagement.controller.inbound;

import com.buildenough.logisticsmanagement.dto.inbound.InboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.inbound.InboundResponse;
import com.buildenough.logisticsmanagement.service.inbound.InboundService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inbounds")
public class InboundController {

    private InboundService inboundService;

    public InboundController(InboundService inboundService) {
        this.inboundService = inboundService;
    }

    @PostMapping
    public ResponseEntity<InboundResponse> createInbound(
            @Valid @RequestBody InboundCreateRequest request
    ) {
        InboundResponse response = inboundService.createInbound(request);
        return ResponseEntity.ok(response);
    }
}
