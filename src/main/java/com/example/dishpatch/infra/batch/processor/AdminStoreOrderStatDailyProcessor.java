package com.example.dishpatch.infra.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminStoreOrderStatDailyProcessor implements ItemProcessor<StoreOrderStatDaily, AdminStoreOrderStatDaily> {

	@Override
	public AdminStoreOrderStatDaily process(StoreOrderStatDaily order) {
		return new AdminStoreOrderStatDaily(order.getDate(), order.getOrderCount(), order.getTotalSales());
	}
}
