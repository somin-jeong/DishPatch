package com.example.dishpatch.infra.batch.writer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatDailyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminStoreOrderDailyWriter implements ItemWriter<AdminStoreOrderStatDaily> {

	private final AdminStoreOrderStatDailyRepository dailyRepository;

	@Override
	public void write(Chunk<? extends AdminStoreOrderStatDaily> items) {
		Map<LocalDate, List<AdminStoreOrderStatDaily>> grouped = items.getItems().stream()
			.collect(Collectors.groupingBy(AdminStoreOrderStatDaily::getDate));

		List<AdminStoreOrderStatDaily> merged = grouped.entrySet().stream()
			.map(e -> {
				int orderCount = e.getValue().stream().mapToInt(AdminStoreOrderStatDaily::getOrderCount).sum();
				long totalSales = e.getValue().stream().mapToLong(AdminStoreOrderStatDaily::getTotalSales).sum();
				return new AdminStoreOrderStatDaily(e.getKey(), orderCount, totalSales);
			})
			.toList();

		dailyRepository.saveAll(merged);
	}
}
