package com.example.dishpatch.domain.statistics.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dishpatch.api.statistics.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.statistics.request.StoreOrderStatRequest;
import com.example.dishpatch.api.statistics.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatDailyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DailyOrderStatStrategy implements StoreOrderStatStrategy {

	private final StoreOrderStatDailyRepository dailyRepo;

	@Override
	public StoreOrderStatPeriodType getPeriodType() {
		return StoreOrderStatPeriodType.DAILY;
	}

	@Override
	public StoreOrderStatResponse getStatistics(Long storeId, StoreOrderStatRequest request) {
		List<StoreOrderStatDaily> stats = dailyRepo.findByStoreIdAndDateRange(
			storeId, request.from(), request.to());
		return toResponse(stats);
	}
}
