package com.example.dishpatch.api.menu.response;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

public record MenuAddResponse(
	Long optionId,
	String name,
	Integer price
) {
	public static MenuAddResponse from(MenuOption menuOption) {
		return new MenuAddResponse(menuOption.getId(), menuOption.getName(), menuOption.getPrice());
	}
}
