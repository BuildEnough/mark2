package com.buildenough.logisticsmanagement.controller.inbound;

import com.buildenough.logisticsmanagement.dto.inbound.InboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.inbound.InboundListItemResponse;
import com.buildenough.logisticsmanagement.dto.inbound.InboundResponse;
import com.buildenough.logisticsmanagement.service.inbound.InboundService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<InboundListItemResponse>> getInbounds(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<InboundListItemResponse> list = inboundService.getInbounds(from, to);
        return ResponseEntity.ok(list);
    }
}
