package com.example.dishpatch.infra.db.statistics.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_stats_monthly")
public class OrderStatMonthly {

    @EmbeddedId
    private OrderStatId id;

    @Column(nullable = false)
    private Integer orderCount;

    @Column(nullable = false)
    private Long totalSales;
}
