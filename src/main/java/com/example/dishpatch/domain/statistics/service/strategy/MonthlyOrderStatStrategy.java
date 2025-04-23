package com.example.dishpatch.domain.statistics.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dishpatch.api.statistics.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.statistics.request.StoreOrderStatRequest;
import com.example.dishpatch.api.statistics.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatMonthlyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MonthlyOrderStatStrategy implements StoreOrderStatStrategy {

	private final StoreOrderStatMonthlyRepository monthlyRepo;

	@Override
	public StoreOrderStatPeriodType getPeriodType() {
		return StoreOrderStatPeriodType.MONTHLY;
	}

	@Override
	public StoreOrderStatResponse getStatistics(Long storeId, StoreOrderStatRequest request) {
		List<StoreOrderStatMonthly> stats = monthlyRepo.findByStoreIdAndDateRange(
			storeId, request.from(), request.to());
		return toResponse(stats);
	}
}