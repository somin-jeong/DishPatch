package com.example.dishpatch.infra.batch.writer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatMonthlyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminStoreOrderMonthlyWriter implements ItemWriter<AdminStoreOrderStatMonthly> {

	private final AdminStoreOrderStatMonthlyRepository monthlyRepository;

	@Override
	public void write(Chunk<? extends AdminStoreOrderStatMonthly> items) {
		Map<LocalDate, List<AdminStoreOrderStatMonthly>> grouped = items.getItems().stream()
			.collect(Collectors.groupingBy(AdminStoreOrderStatMonthly::getDate));

		List<AdminStoreOrderStatMonthly> merged = grouped.entrySet().stream()
			.map(e -> {
				int orderCount = e.getValue().stream().mapToInt(AdminStoreOrderStatMonthly::getOrderCount).sum();
				long totalSales = e.getValue().stream().mapToLong(AdminStoreOrderStatMonthly::getTotalSales).sum();
				return new AdminStoreOrderStatMonthly(e.getKey(), orderCount, totalSales);
			})
			.toList();

		monthlyRepository.saveAll(merged);
	}
}
