package com.example.dishpatch.infra.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;

@Component
public class StoreOrderStatDailyProcessor implements ItemProcessor<Order, StoreOrderStatDaily> {

	@Override
	public StoreOrderStatDaily process(Order order) {
		StoreOrderStatId id = StoreOrderStatId.of(order.getStore().getId(), order.getCreatedDate().toLocalDate());
		return StoreOrderStatDaily.of(id, 1, Long.valueOf(order.getTotalPrice()));
	}
}
