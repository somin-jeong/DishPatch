package com.example.dishpatch.api.statistics.response;

import java.time.LocalDate;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

public record StoreOrderStatItem(
	LocalDate date,
	Integer orderCount,
	Long totalSales
) {

	public static StoreOrderStatItem from(StoreOrderStatDaily dailyStat) {
		return new StoreOrderStatItem(
			dailyStat.getId().getDate(),
			dailyStat.getOrderCount(),
			dailyStat.getTotalSales()
		);
	}

	public static StoreOrderStatItem from(StoreOrderStatMonthly monthlyStat) {
		return new StoreOrderStatItem(
			monthlyStat.getId().getDate(),
			monthlyStat.getOrderCount(),
			monthlyStat.getTotalSales()
		);
	}
}
