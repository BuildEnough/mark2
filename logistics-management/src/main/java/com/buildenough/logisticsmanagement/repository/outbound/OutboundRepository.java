package com.buildenough.logisticsmanagement.repository.outbound;

import com.buildenough.logisticsmanagement.domain.outbound.Outbound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {
    List<Outbound> findByOutboundDateBetween(LocalDateTime start, LocalDateTime end);
}
