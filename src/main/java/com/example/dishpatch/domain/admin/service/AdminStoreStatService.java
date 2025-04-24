package com.example.dishpatch.domain.admin.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.admin.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.admin.request.StoreOrderStatRequest;
import com.example.dishpatch.api.admin.response.StoreOrderStatResponse;
import com.example.dishpatch.domain.admin.exception.StatErrorCode;
import com.example.dishpatch.domain.admin.service.strategy.AdminStoreOrderStatStrategy;
import com.example.dishpatch.global.exception.BizException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminStoreStatService {

	private final List<AdminStoreOrderStatStrategy> storeOrderStatStrategies;

	private Map<StoreOrderStatPeriodType, AdminStoreOrderStatStrategy> storeOrderStatMap;

	@PostConstruct
	private void initStrategyMap() {
		this.storeOrderStatMap = storeOrderStatStrategies.stream()
			.collect(Collectors.toMap(
				AdminStoreOrderStatStrategy::getPeriodType,
				Function.identity()
			));
	}

	public StoreOrderStatResponse getOrderStat(StoreOrderStatRequest req) {
		AdminStoreOrderStatStrategy strategy = storeOrderStatMap.get(req.period());
		if (strategy == null) {
			throw new BizException(StatErrorCode.UNSUPPORTED_STAT_PERIOD);
		}
		return strategy.getStatistics(req);
	}
}
