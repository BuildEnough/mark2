package com.buildenough.logisticsmanagement.domain.inbound;

import com.buildenough.logisticsmanagement.domain.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
@Table(name = "inbounds")
public class Inbound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "warehouseId", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private LocalDateTime inboundDate;

    @Column(nullable = false)
    private String remark; // 비고

    @OneToMany(mappedBy = "inbound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InboundItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Inbound(Warehouse warehouse, LocalDateTime inboundDate, String remark) {
        this.warehouse = warehouse;
        this.inboundDate = inboundDate;
        this.remark = remark;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(InboundItem item) {
        this.items.add(item);
        item.setInbound(this);
    }

}
