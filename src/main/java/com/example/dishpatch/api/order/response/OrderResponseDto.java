package com.example.dishpatch.api.order.response;

import java.util.List;

import com.example.dishpatch.infra.db.order.entity.OrderStatus;

public record OrderResponseDto(
	Long orderId,
	Long userId,
	Long storeId,
	List<Long> orderItemIds,
	Integer totalPrice,
	OrderStatus status
) {

}
