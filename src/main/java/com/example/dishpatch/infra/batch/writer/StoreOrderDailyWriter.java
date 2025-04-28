package com.example.dishpatch.infra.batch.writer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.batch.service.StoreOrderStatBatchService;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreOrderDailyWriter implements ItemWriter<StoreOrderStatDaily> {

	private final StoreOrderStatBatchService service;

	@Override
	public void write(Chunk<? extends StoreOrderStatDaily> items) {
		Map<StoreOrderStatId, List<StoreOrderStatDaily>> grouped = items.getItems().stream()
			.collect(Collectors.groupingBy(StoreOrderStatDaily::getId));

		List<StoreOrderStatDaily> merged = grouped.entrySet().stream()
			.map(e -> {
				int orderCount = e.getValue().stream().mapToInt(StoreOrderStatDaily::getOrderCount).sum();
				long totalSales = e.getValue().stream().mapToLong(StoreOrderStatDaily::getTotalSales).sum();
				return StoreOrderStatDaily.of(e.getKey(), orderCount, totalSales);
			})
			.toList();

		service.saveDaily(merged);
	}
}
