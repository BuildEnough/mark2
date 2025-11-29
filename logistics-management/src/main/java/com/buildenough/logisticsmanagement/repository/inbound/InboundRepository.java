package com.buildenough.logisticsmanagement.repository.inbound;

import com.buildenough.logisticsmanagement.domain.inbound.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundRepository extends JpaRepository<Inbound, Long> {

}
