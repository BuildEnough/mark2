package com.buildenough.logisticsmanagement.domain.outbound;

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
@Table(name = "outbounds")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
public class Outbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 창고에서 출고되는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private LocalDateTime outboundDate;

    @Column(length = 255)
    private String customerName; // 출고 대상

    @Column(length = 255)
    private String remark;

    @OneToMany(mappedBy = "outbound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutboundItem> items = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    public Outbound(LocalDateTime outboundDate, Warehouse warehouse, String customerName, String remark) {
        this.outboundDate = outboundDate;
        this.warehouse = warehouse;
        this.customerName = customerName;
        this.remark = remark;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = this.createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void addItem(OutboundItem item) {
        this.items.add(item);
        item.setOutbound(this);
    }
}
