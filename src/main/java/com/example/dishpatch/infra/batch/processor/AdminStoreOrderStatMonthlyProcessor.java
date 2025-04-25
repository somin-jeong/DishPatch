package com.example.dishpatch.infra.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminStoreOrderStatMonthlyProcessor
	implements ItemProcessor<StoreOrderStatMonthly, AdminStoreOrderStatMonthly> {

	@Override
	public AdminStoreOrderStatMonthly process(StoreOrderStatMonthly order) {
		return new AdminStoreOrderStatMonthly(order.getDate(), order.getOrderCount(), order.getTotalSales());
	}
}
