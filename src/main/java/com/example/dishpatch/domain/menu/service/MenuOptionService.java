package com.example.dishpatch.domain.menu.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.menu.request.MenuAddRequest;
import com.example.dishpatch.api.menu.response.MenuAddResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
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
	public MenuAddResponse addMenuOption(Long userId, Long menuId, MenuAddRequest req) {
		Menu menu = menuRepository.findByMenuId(menuId)
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

		if (!Objects.equals(menu.getStore().getId(), userId)) {
			// todo : 가게의 주인이 아니다 라는 예외로 변경해야함
			throw new BizException(StoreErrorCode.STORE_NOT_FOUND);
		}

		MenuOption menuOption = MenuOption.builder()
			.name(req.name())
			.price(req.price())
			.menu(menu)
			.build();
		menuOptionRepository.save(menuOption);

		return MenuAddResponse.from(menuOption);
	}
}
