package com.example.dishpatch.infra.batch.processor;

import java.time.LocalDate;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

@Component
public class StoreOrderStatMonthlyProcessor implements ItemProcessor<StoreOrderStatDaily, StoreOrderStatMonthly> {

	@Override
	public StoreOrderStatMonthly process(StoreOrderStatDaily item) {
		Long storeId = item.getId().getStoreId();
		LocalDate date = item.getId().getDate().withDayOfMonth(1);

		StoreOrderStatId id = StoreOrderStatId.of(storeId, date);
		return StoreOrderStatMonthly.of(id, item.getOrderCount(), item.getTotalSales());
	}
}
