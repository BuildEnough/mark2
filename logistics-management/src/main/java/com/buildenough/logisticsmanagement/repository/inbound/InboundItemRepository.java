package com.buildenough.logisticsmanagement.repository.inbound;

import com.buildenough.logisticsmanagement.domain.inbound.InboundItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundItemRepository extends JpaRepository<InboundItem, Long> {
}
