package com.buildenough.logisticsmanagement.controller.outbound;

import com.buildenough.logisticsmanagement.dto.outbound.OutboundCreateRequest;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundListItemResponse;
import com.buildenough.logisticsmanagement.dto.outbound.OutboundResponse;
import com.buildenough.logisticsmanagement.service.outbound.OutboundService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<OutboundListItemResponse>> getOutbounds(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<OutboundListItemResponse> list = outboundService.getOutbounds(from, to);
        return ResponseEntity.ok(list);
    }
}
