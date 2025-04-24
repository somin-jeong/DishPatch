package com.example.dishpatch.api.cart.response;

import com.example.dishpatch.infra.db.cart.entity.Cart;

public record CartCreateResponse(
	Long id,
	Long storeId,
	Long menuId,
	Long menuOptionId,
	int quantity
) {
	public static CartCreateResponse from(Cart cart) {
		return new CartCreateResponse(
			cart.getId(),
			cart.getStore().getId(),
			cart.getMenu().getId(),
			cart.getMenuOption().getId(),
			cart.getQuantity()
		);
	}
}
