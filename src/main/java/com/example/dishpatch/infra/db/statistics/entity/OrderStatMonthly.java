package com.example.dishpatch.infra.db.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
