package com.example.dishpatch.domain.admin.service.strategy;

import java.util.List;

import com.example.dishpatch.api.admin.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.admin.request.StoreOrderStatRequest;
import com.example.dishpatch.api.admin.response.StoreOrderStatItem;
import com.example.dishpatch.api.admin.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.common.StatConvertible;

public interface StoreOrderStatStrategy {
	StoreOrderStatPeriodType getPeriodType();

	StoreOrderStatResponse getStatistics(StoreOrderStatRequest request);

	default StoreOrderStatResponse toResponse(List<? extends StatConvertible> stats) {
		List<StoreOrderStatItem> items = stats.stream()
			.map(stat -> new StoreOrderStatItem(
				stat.getDate(),
				stat.getOrderCount(),
				stat.getTotalSales()
			))
			.toList();

		return new StoreOrderStatResponse(items);
	}
}
