package com.example.dishpatch.domain.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.entity.OrderItem;
import com.example.dishpatch.infra.db.order.repository.OrderItemRepository;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final MenuOptionRepository menuOptionRepository;

	public List<Long> addOrderItem(Long orderId, List<CartResponseDto> cartResponseDtoList) {

		List<Long> addedIds = new ArrayList<>();

		for (CartResponseDto cartResponseDto : cartResponseDtoList) {

			Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

			Menu menu = menuRepository.findById(cartResponseDto.menuId())
				.orElseThrow(() -> new RuntimeException("메뉴가 존재하지 않습니다."));

			MenuOption menuOption = menuOptionRepository(cartResponseDto.menuOptionId())
				.orElse(null);

			OrderItem orderItem = new OrderItem(cartResponseDto.quantity(), order, menu, menuOption);

			OrderItem savedOrderItem = orderItemRepository.save(orderItem);

			addedIds.add(savedOrderItem.getId());
		}
		return addedIds;
	}

	public List<Long> getOrderItems(Long orderId) {

		return orderItemRepository.findIdsByOrderId(orderId);
	}
}
