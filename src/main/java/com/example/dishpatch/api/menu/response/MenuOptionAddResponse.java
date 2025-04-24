package com.example.dishpatch.api.menu.response;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

public record MenuOptionAddResponse(
	Long optionId,
	String name,
	Integer price
) {
	public static MenuOptionAddResponse from(MenuOption menuOption) {
		return new MenuOptionAddResponse(menuOption.getId(), menuOption.getName(), menuOption.getPrice());
	}
}
