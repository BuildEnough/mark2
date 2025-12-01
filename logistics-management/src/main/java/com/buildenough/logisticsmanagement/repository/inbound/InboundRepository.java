package com.buildenough.logisticsmanagement.repository.inbound;

import com.buildenough.logisticsmanagement.domain.inbound.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InboundRepository extends JpaRepository<Inbound, Long> {
    List<Inbound> findByInboundDateBetween(LocalDateTime start, LocalDateTime end);
}
