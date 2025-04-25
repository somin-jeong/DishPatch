package com.example.dishpatch.infra.batch.reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StoreOrderStatDailyReader implements ItemReader<Order> {

	private final OrderRepository orderRepository;
	private Iterator<Order> orderIterator;

	@Override
	public Order read() {
		if (orderIterator == null) {
			LocalDate targetDate = LocalDate.now().minusDays(1);
			LocalDateTime from = targetDate.atStartOfDay();
			LocalDateTime to = targetDate.plusDays(1).atStartOfDay();

			List<Order> orders
				= orderRepository.findAllByStatusAndCreatedDateRange(OrderStatus.FINISHED, from, to);
			orderIterator = orders.iterator();
		}
		return orderIterator.hasNext() ? orderIterator.next() : null;
	}
}
