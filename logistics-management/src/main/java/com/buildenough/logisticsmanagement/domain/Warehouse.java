package com.buildenough.logisticsmanagement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@Getter @Setter
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // 창고 코드

    @Column(nullable = false, length = 100)
    private String name; // 창고 이름

    @Column(length = 255)
    private String location; // 위치

    @Column(length = 255)
    private String description; // 비고

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WarehouseStatus status;

    public Warehouse(String code, String name, String location, String description) {
        this.code = code;
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = WarehouseStatus.ACTIVE; // 기본값
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.status == null) {
            this.status = WarehouseStatus.ACTIVE;
        }
    }

    // 비활성화 메서드
    public void deactivate() {
        this.status = WarehouseStatus.INACTIVE;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
