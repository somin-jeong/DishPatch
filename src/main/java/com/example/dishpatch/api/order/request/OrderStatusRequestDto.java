package com.example.dishpatch.api.order.request;

import com.example.dishpatch.infra.db.order.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

	@NotNull(message = "주문 상태는 필수 입력 값입니다.")
	private OrderStatus orderStatus;

}
