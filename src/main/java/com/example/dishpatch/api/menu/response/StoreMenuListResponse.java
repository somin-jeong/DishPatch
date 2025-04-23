package com.example.dishpatch.api.menu.response;

import java.util.List;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public record StoreMenuListResponse(
	List<MenuResponse> menus
) {

	public static StoreMenuListResponse from(List<Menu> menus) {
		return new StoreMenuListResponse(
			menus.stream()
				.map(MenuResponse::from)
				.toList()
		);
	}
}
