package com.example.dishpatch.api.menu.response;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;

public record MenuOptionResponse(
	Long optionId,
	String name,
	Integer price,
	boolean soldOut
) {

	public static MenuOptionResponse from(MenuOption option) {
		return new MenuOptionResponse(option.getId(), option.getName(), option.getPrice(), option.isSoldOut());
	}
}
