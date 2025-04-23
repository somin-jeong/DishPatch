package com.example.dishpatch.api.menu.response;

import java.util.List;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public record MenuResponse(
	Long menuId,
	String name,
	Integer price,
	boolean soldOut,
	List<MenuOptionResponse> options
) {

	public static MenuResponse from(Menu menu) {
		return new MenuResponse(
			menu.getId(),
			menu.getName(),
			menu.getPrice(),
			menu.isSoldOut(),
			menu.getOptions().stream()
				.map(MenuOptionResponse::from)
				.toList()
		);
	}
}
