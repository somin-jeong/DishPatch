package com.example.dishpatch.api.order.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.dishpatch.infra.db.order.entity.OrderStatus;

public record OrderDetailResponseDto(
	Long orderId,
	Long userId,
	Long storeId,
	List<OrderItemDetailResponseDto> orderItemList,
	Integer totalPrice,
	OrderStatus status,
	LocalDateTime createdDate
) {

}
