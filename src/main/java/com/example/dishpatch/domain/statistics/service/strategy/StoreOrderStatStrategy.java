package com.example.dishpatch.domain.statistics.service.strategy;

import java.util.List;

import com.example.dishpatch.api.statistics.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.statistics.request.StoreOrderStatRequest;
import com.example.dishpatch.api.statistics.response.StoreOrderStatItem;
import com.example.dishpatch.api.statistics.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.common.StatConvertible;

public interface StoreOrderStatStrategy {
	StoreOrderStatPeriodType getPeriodType();

	StoreOrderStatResponse getStatistics(Long storeId, StoreOrderStatRequest request);

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
