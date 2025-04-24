package com.example.dishpatch.infra.batch.writer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatMonthlyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreOrderMonthlyWriter implements ItemWriter<StoreOrderStatMonthly> {

	private final StoreOrderStatMonthlyRepository monthlyRepository;

	@Override
	public void write(Chunk<? extends StoreOrderStatMonthly> items) {
		Map<StoreOrderStatId, StoreOrderStatMonthly> grouped = items.getItems().stream()
			.collect(Collectors.toMap(
				StoreOrderStatMonthly::getId,
				stat -> stat,
				(s1, s2) -> {
					s1.add(s2.getOrderCount(), s2.getTotalSales());
					return s1;
				}
			));

		List<StoreOrderStatMonthly> merged = grouped.values().stream().toList();
		monthlyRepository.saveAll(merged);
	}
}
