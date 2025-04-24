package com.example.dishpatch.domain.statistics.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.statistics.request.StoreOrderStatPeriodType;
import com.example.dishpatch.api.statistics.request.StoreOrderStatRequest;
import com.example.dishpatch.api.statistics.response.StoreOrderStatResponse;
import com.example.dishpatch.domain.statistics.exception.StatErrorCode;
import com.example.dishpatch.domain.statistics.service.strategy.StoreOrderStatStrategy;
import com.example.dishpatch.global.exception.BizException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreOrderStatService {

	private final List<StoreOrderStatStrategy> storeOrderStatStrategies;

	private Map<StoreOrderStatPeriodType, StoreOrderStatStrategy> storeOrderStatMap;

	@PostConstruct
	private void initStrategyMap() {
		this.storeOrderStatMap = storeOrderStatStrategies.stream()
			.collect(Collectors.toMap(
				StoreOrderStatStrategy::getPeriodType,
				Function.identity()
			));
	}

	public StoreOrderStatResponse getOrderStatistics(Long storeId, StoreOrderStatRequest req) {
		StoreOrderStatStrategy strategy = storeOrderStatMap.get(req.period());
		if (strategy == null) {
			throw new BizException(StatErrorCode.UNSUPPORTED_STAT_PERIOD);
		}
		return strategy.getStatistics(storeId, req);
	}
}
