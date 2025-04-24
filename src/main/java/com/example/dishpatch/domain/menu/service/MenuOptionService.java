package com.example.dishpatch.domain.menu.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.menu.request.MenuOptionAddRequest;
import com.example.dishpatch.api.menu.request.MenuOptionUpdateRequest;
import com.example.dishpatch.api.menu.response.MenuOptionAddResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.menu.exception.MenuOptionErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MenuOptionService {

	private final MenuOptionRepository menuOptionRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public MenuOptionAddResponse addMenuOption(Long userId, Long menuId, MenuOptionAddRequest req) {
		Menu menu = menuRepository.findByMenuId(menuId)
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

		validateStoreOwner(userId, menu);

		MenuOption menuOption = MenuOption.builder()
			.name(req.name())
			.price(req.price())
			.menu(menu)
			.build();
		menuOptionRepository.save(menuOption);

		return MenuOptionAddResponse.from(menuOption);
	}

	@Transactional
	public void updateMenuOption(Long userId, Long menuId, Long optionId, MenuOptionUpdateRequest req) {
		MenuOption option = menuOptionRepository.findByIdWithMenuAndStore(optionId)
			.orElseThrow(() -> new BizException(MenuOptionErrorCode.MENU_OPTION_NOT_FOUND));

		validateOptionMenuMatch(menuId, option);
		validateStoreOwner(userId, option.getMenu());

		option.update(req.name(), req.price(), req.soldOut());
	}

	private static void validateOptionMenuMatch(Long menuId, MenuOption option) {
		if (!Objects.equals(option.getMenu().getId(), menuId)) {
			throw new BizException(MenuOptionErrorCode.MENU_OPTION_MISMATCH);
		}
	}

	private static void validateStoreOwner(Long userId, Menu menu) {
		if (!Objects.equals(menu.getStore().getUser().getId(), userId)) {
			throw new BizException(StoreErrorCode.STORE_OWNER_MISMATCH);
		}
	}

}
