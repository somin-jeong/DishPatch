package com.example.dishpatch.domain.admin.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dishpatch.api.admin.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.admin.request.StoreOrderStatRequest;
import com.example.dishpatch.api.admin.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatMonthlyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MonthlyOrderStatStrategy implements StoreOrderStatStrategy {

	private final AdminStoreOrderStatMonthlyRepository monthlyRepo;

	@Override
	public StoreOrderStatPeriodType getPeriodType() {
		return StoreOrderStatPeriodType.MONTHLY;
	}

	@Override
	public StoreOrderStatResponse getStatistics(StoreOrderStatRequest request) {
		List<AdminStoreOrderStatMonthly> stats = monthlyRepo.findAllByDateRange(request.from(), request.to());
		return toResponse(stats);
	}
}
