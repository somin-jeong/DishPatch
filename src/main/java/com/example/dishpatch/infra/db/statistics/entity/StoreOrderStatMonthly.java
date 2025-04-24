package com.example.dishpatch.infra.db.statistics.entity;

import java.time.LocalDate;

import com.example.dishpatch.infra.db.common.StatConvertible;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "store_order_stats_monthly")
public class StoreOrderStatMonthly implements StatConvertible {

	@EmbeddedId
	private StoreOrderStatId id;

	@Column(nullable = false)
	private Integer orderCount;

	@Column(nullable = false)
	private Long totalSales;

	@Override
	public LocalDate getDate() {
		return this.id.getDate();
	}
}
