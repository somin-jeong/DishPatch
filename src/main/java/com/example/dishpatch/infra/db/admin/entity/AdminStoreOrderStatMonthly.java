package com.example.dishpatch.infra.db.admin.entity;

import java.time.LocalDate;

import com.example.dishpatch.infra.db.common.StatConvertible;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "admin_store_order_stats_monthly")
public class AdminStoreOrderStatMonthly extends AdminStoreStat implements StatConvertible {

	public AdminStoreOrderStatMonthly(LocalDate date, Integer orderCount, Long totalSales) {
		super(date, orderCount, totalSales);
	}
}
