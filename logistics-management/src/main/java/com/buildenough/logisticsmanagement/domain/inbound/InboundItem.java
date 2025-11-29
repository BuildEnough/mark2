package com.buildenough.logisticsmanagement.domain.inbound;

import com.buildenough.logisticsmanagement.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inbound_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
public class InboundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부모 입고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbound_id", nullable = false)
    private Inbound inbound;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    public InboundItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // 연관관계 Inbound
    void setInbound(Inbound inbound) {
        this.inbound = inbound;
    }
}
