package com.example.dishpatch.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.api.menu.response.StoreMenuListResponse;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@Test
	void createMenu_shouldSucceed() {
		Long storeId = 1L;
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴 이름", 10000, "https://image.com/image_url");

		given(storeRepository.existsById(storeId)).willReturn(true);

		MenuCreateResponse res = menuService.createMenu(storeId, menuCreateRequest);

		verify(menuRepository, times(1)).save(any());
		assertEquals(res.name(), menuCreateRequest.name());
		assertEquals(res.price(), menuCreateRequest.price());
		assertEquals(res.imageUrl(), menuCreateRequest.imageUrl());
	}

	@Test
	void createMenu_whenStoreNotFound_shouldThrowException() {
		Long storeId = 1L;
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴 이름", 10000, "https://image.com/image_url");

		given(storeRepository.existsById(storeId)).willReturn(false);

		RuntimeException exception = assertThrows(RuntimeException.class,
			() -> menuService.createMenu(storeId, menuCreateRequest));
		assertEquals("존재하지 않는 가게입니다.", exception.getMessage());
	}

	@Test
	void getStoreMenus_shouldSucceed() {
		Long storeId = 1L;
		List<Menu> menus = List.of(
			// new Menu("메뉴 이름1", 10000, "https://image.com/image_url1", false, new Store()),
			// new Menu("메뉴 이름2", 20000, "https://image.com/image_url2", true, new Store())
		);

		given(storeRepository.existsById(storeId)).willReturn(true);
		given(menuRepository.findAllByStoreId(1L)).willReturn(menus);

		StoreMenuListResponse res = menuService.getStoreMenus(storeId);

		assertEquals(2, res.menus().size());
		assertEquals("메뉴 이름1", res.menus().get(0).name());
		assertEquals("메뉴 이름2", res.menus().get(1).name());
	}

	@Test
	void getStoreMenus_whenStoreNotFound_shouldThrowException() {
		Long storeId = 1L;

		given(storeRepository.existsById(storeId)).willReturn(false);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> menuService.getStoreMenus(storeId));
		assertEquals("존재하지 않는 가게입니다.", exception.getMessage());
	}
}
