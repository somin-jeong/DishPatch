package com.example.dishpatch.infra.batch.reader;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatDailyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StoreOrderStatMonthlyReader implements ItemReader<StoreOrderStatDaily> {

	private final StoreOrderStatDailyRepository dailyRepository;
	private Iterator<StoreOrderStatDaily> dailyIterator;

	@Override
	public StoreOrderStatDaily read() {
		if (dailyIterator == null) {
			LocalDate now = LocalDate.now();
			LocalDate from = now.minusDays(1).withDayOfMonth(1);
			LocalDate to = now.plusMonths(1).withDayOfMonth(1);
			List<StoreOrderStatDaily> items = dailyRepository.findAllByDateRange(from, to);
			dailyIterator = items.iterator();
		}
		return dailyIterator.hasNext() ? dailyIterator.next() : null;
	}
}
