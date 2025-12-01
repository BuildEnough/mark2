package com.buildenough.logisticsmanagement.domain.outbound;

import com.buildenough.logisticsmanagement.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "outbound_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
public class OutboundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부모 출고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_id", nullable = false)
    private Outbound outbound;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    public OutboundItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    void setOutbound(Outbound outbound) {
        this.outbound = outbound;
    }
}
