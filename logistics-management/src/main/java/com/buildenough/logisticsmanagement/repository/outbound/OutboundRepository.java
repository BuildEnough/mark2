package com.buildenough.logisticsmanagement.repository.outbound;

import com.buildenough.logisticsmanagement.domain.outbound.Outbound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {
}
