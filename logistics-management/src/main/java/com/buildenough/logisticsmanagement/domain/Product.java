package com.buildenough.logisticsmanagement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@Entity
@Getter @Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 increment
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // 상품 코드

    @Column(nullable = false, length = 100)
    private String name; // 상품명

    @Column(nullable = false, length = 20)
    private String unit; //단위(box, ex)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status; // ACTIVE, INACTIVE

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Product(String code, String name, String unit, ProductStatus status) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.status = status;
    }

    // createdAt, updatedAt 자동으로 입력
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt; // 처음엔 둘이 시간 같아야해서 이렇게 함

        if(this.status == null) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

}
