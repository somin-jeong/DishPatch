package com.example.dishpatch.api.menu.response;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public record MenuCreateResponse(
	Long menuId,
	String name,
	Integer price,
	String imageUrl
) {
	public static MenuCreateResponse from(Menu menu) {
		return new MenuCreateResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getImageUrl());
	}
}
