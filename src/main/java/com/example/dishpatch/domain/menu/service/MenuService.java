package com.example.dishpatch.domain.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.api.menu.response.StoreMenuListResponse;
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

	public MenuCreateResponse createMenu(Long storeId, MenuCreateRequest req) {
		if (!storeRepository.existsById(storeId)) {
			// todo : Store 의 ErrorCode 가 생기면 수정
			throw new RuntimeException("존재하지 않는 가게입니다.");
		}

		// todo : 가게의 주인이 맞는지 검증

		Store store = storeRepository.getReferenceById(storeId);
		Menu menu = Menu.builder()
			.name(req.name())
			.price(req.price())
			.imageUrl(req.imageUrl())
			.store(store)
			.build();

		menuRepository.save(menu);

		return MenuCreateResponse.from(menu);
	}

	public StoreMenuListResponse getStoreMenus(Long storeId) {
		if (!storeRepository.existsById(storeId)) {
			// todo : Store 의 ErrorCode 가 생기면 수정
			throw new RuntimeException("존재하지 않는 가게입니다.");
		}

		List<Menu> menus = menuRepository.findAllByStoreId(storeId);
		return StoreMenuListResponse.from(menus);
	}
}
