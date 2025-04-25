package com.example.dishpatch.api.cart.response;

import com.example.dishpatch.infra.db.cart.entity.Cart;

public record CartItemResponse(
	Long id,
	Long menuId,
	String menuName,
	int menuPrice,
	Long menuOptionId,
	String menuOptionName,
	int menuOptionPrice,
	int quantity
) {
	public static CartItemResponse from(Cart cart) {
		return new CartItemResponse(
			cart.getId(),
			cart.getMenu().getId(),
			cart.getMenu().getName(),
			cart.getMenu().getPrice(),
			cart.getMenuOption().getId(),
			cart.getMenuOption().getName(),
			cart.getMenuOption().getPrice(),
			cart.getQuantity()
		);
	}
}
