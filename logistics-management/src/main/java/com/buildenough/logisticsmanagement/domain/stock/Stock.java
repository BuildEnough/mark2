package com.buildenough.logisticsmanagement.domain.stock;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@Table(
        name = "stocks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stock_warehouse_product", // 에러 이름
                        columnNames = {"warehouse_id", "product_id"} // 하나의 창고에 하나의 상품에 대한 재고는 한 줄만 존재
                )
        }
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 창고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false) // FK 설정
    private Warehouse warehouse;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 수량
    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Stock(Warehouse warehouse, Product product, Long quantity) {
        this.warehouse = warehouse;
        this.product = product;
        this.quantity = quantity;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.quantity == null) {
            this.quantity = 0L;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void increase(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("입고 수량은 0보다 커야 합니다");
        }
        this.quantity += amount;
    }

    public void decrease(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("출고 수량은 0보다 커야 합니다");
        }
        if (this.quantity - amount < 0) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 수량: " +  this.quantity);
        }

        this.quantity += amount;
    }
}
