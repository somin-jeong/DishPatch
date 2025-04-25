package com.example.dishpatch.infra.batch.reader;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatMonthlyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminStoreOrderStatMonthlyReader implements ItemReader<StoreOrderStatMonthly> {

	private final StoreOrderStatMonthlyRepository monthlyRepository;
	private Iterator<StoreOrderStatMonthly> monthlyIterator;

	@Override
	public StoreOrderStatMonthly read() {
		if (monthlyIterator == null) {
			LocalDate targetDate = LocalDate.now().minusMonths(1);

			List<StoreOrderStatMonthly> stats
				= monthlyRepository.findAllByDate(targetDate);
			monthlyIterator = stats.iterator();
		}
		return monthlyIterator.hasNext() ? monthlyIterator.next() : null;
	}
}
