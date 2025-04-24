package com.example.dishpatch.domain.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.order.exception.OrderErrorCode;
import com.example.dishpatch.global.exception.BizException;
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
				.orElseThrow(() -> new BizException(OrderErrorCode.ORDER_NOT_FOUND));

			Menu menu = menuRepository.findById(cartResponseDto.menuId())
				.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

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
