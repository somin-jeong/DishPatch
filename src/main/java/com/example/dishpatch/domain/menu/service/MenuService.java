package com.example.dishpatch.domain.menu.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.request.MenuUpdateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.api.menu.response.StoreMenuListResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MenuService {

	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public MenuCreateResponse createMenu(Long userId, Long storeId, MenuCreateRequest req) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		if (!Objects.equals(store.getUser().getId(), userId)) {
			// todo : 가게의 주인이 아니다 라는 예외로 변경해야함
			throw new BizException(StoreErrorCode.STORE_NOT_FOUND);
		}

		Menu menu = Menu.builder()
			.name(req.name())
			.price(req.price())
			.imageUrl(req.imageUrl())
			.store(store)
			.build();

		menuRepository.save(menu);

		return MenuCreateResponse.from(menu);
	}

	@Transactional(readOnly = true)
	public StoreMenuListResponse getStoreMenus(Long storeId) {
		if (!storeRepository.existsById(storeId)) {
			throw new BizException(StoreErrorCode.STORE_NOT_FOUND);
		}

		List<Menu> menus = menuRepository.findAllByStoreId(storeId);
		return StoreMenuListResponse.from(menus);
	}

	@Transactional
	public void updateMenu(Long userId, Long storeId, Long menuId, MenuUpdateRequest req) {
		Menu menu = findMenuOrThrow(menuId);

		validateMenuStoreMatch(storeId, menu);
		validateMenuOwner(userId, menu);

		menu.update(req.name(), req.price(), req.imageUrl(), req.soldOut());
	}

	@Transactional
	public void deleteMenu(Long userId, Long storeId, Long menuId) {
		Menu menu = findMenuOrThrow(menuId);

		validateMenuStoreMatch(storeId, menu);
		validateMenuOwner(userId, menu);

		menu.softDelete();
	}

	private void validateMenuStoreMatch(Long storeId, Menu menu) {
		if (!Objects.equals(menu.getStore().getId(), storeId)) {
			throw new BizException(MenuErrorCode.MENU_STORE_MISMATCH);
		}
	}

	private void validateMenuOwner(Long userId, Menu menu) {
		if (!Objects.equals(menu.getStore().getUser().getId(), userId)) {
			throw new BizException(MenuErrorCode.MENU_OWNER_MISMATCH);
		}
	}

	private Menu findMenuOrThrow(Long menuId) {
		return menuRepository.findByMenuId(menuId)
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));
	}
}
