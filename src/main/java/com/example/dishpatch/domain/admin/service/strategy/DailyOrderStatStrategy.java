package com.example.dishpatch.domain.admin.service.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dishpatch.api.admin.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.admin.request.StoreOrderStatRequest;
import com.example.dishpatch.api.admin.response.StoreOrderStatResponse;
import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatDailyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DailyOrderStatStrategy implements StoreOrderStatStrategy {

	private final AdminStoreOrderStatDailyRepository dailyRepo;

	@Override
	public StoreOrderStatPeriodType getPeriodType() {
		return StoreOrderStatPeriodType.DAILY;
	}

	@Override
	public StoreOrderStatResponse getStatistics(StoreOrderStatRequest request) {
		List<AdminStoreOrderStatDaily> stats = dailyRepo.findAllByDateRange(request.from(), request.to());
		return toResponse(stats);
	}
}
