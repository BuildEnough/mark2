package com.buildenough.logisticsmanagement.repository.outbound;

import com.buildenough.logisticsmanagement.domain.outbound.OutboundItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundItemRepository extends JpaRepository<OutboundItem, Long> {
}
