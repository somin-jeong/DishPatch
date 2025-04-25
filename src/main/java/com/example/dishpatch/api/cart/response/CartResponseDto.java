package com.example.dishpatch.api.cart.response;

import java.util.List;

import com.example.dishpatch.infra.db.cart.entity.Cart;

public record CartResponseDto(
	Long storeId,
	List<CartItemResponse> cartItems,
	int totalPrice
) {
	public static CartResponseDto from(List<Cart> cartList) {

		Long storeId = cartList.isEmpty() ? null : cartList.get(0).getStore().getId();

		List<CartItemResponse> items = cartList.stream()
			.map(CartItemResponse::from)
			.toList();

		int total = cartList.stream()
			.mapToInt(cart -> (cart.getMenu().getPrice() + cart.getMenuOption().getPrice()) * cart.getQuantity())
			.sum();

		return new CartResponseDto(storeId, items, total);
	}
}
