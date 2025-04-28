package com.example.dishpatch.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.request.MenuUpdateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
import com.example.dishpatch.api.menu.response.StoreMenuListResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;

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
		Long userId = 1L;
		Long storeId = 1L;
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴 이름", 10000, "https://image.com/image_url");

		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);

		Store store = Store.builder().user(user).build();
		ReflectionTestUtils.setField(store, "id", storeId);

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		MenuCreateResponse res = menuService.createMenu(userId, storeId, menuCreateRequest);

		verify(menuRepository, times(1)).save(any());
		assertEquals(res.name(), menuCreateRequest.name());
		assertEquals(res.price(), menuCreateRequest.price());
		assertEquals(res.imageUrl(), menuCreateRequest.imageUrl());
	}

	@Test
	void createMenu_whenStoreNotFound_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴 이름", 10000, "https://image.com/image_url");

		given(storeRepository.findById(storeId)).willReturn(Optional.empty());

		BizException exception = assertThrows(BizException.class,
			() -> menuService.createMenu(userId, 1L, menuCreateRequest));
		assertEquals(StoreErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void createMenu_whenNotStoreOwner_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴 이름", 10000, "https://image.com/image_url");

		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);

		Store store = Store.builder().user(user).build();
		ReflectionTestUtils.setField(store, "id", storeId);

		given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

		BizException exception = assertThrows(BizException.class,
			() -> menuService.createMenu(2L, storeId, menuCreateRequest));
		assertEquals(StoreErrorCode.STORE_OWNER_MISMATCH, exception.getErrorCode());
	}

	@Test
	void getStoreMenus_shouldSucceed() {
		Long storeId = 1L;
		Store store = mock(Store.class);
		MenuOption menuOption1 = MenuOption.builder().name("메뉴 옵션 이름1").price(3000).soldOut(false).build();
		MenuOption menuOption2 = MenuOption.builder().name("메뉴 옵션 이름2").price(5000).soldOut(true).build();
		List<Menu> menus = List.of(
			new Menu("메뉴 이름1", 10000, "https://image.com/image_url1", false, store, List.of(menuOption1)),
			new Menu("메뉴 이름2", 20000, "https://image.com/image_url2", true, store, List.of(menuOption2))
		);

		given(storeRepository.existsById(storeId)).willReturn(true);
		given(menuRepository.findAllByStoreIdWithOptions(1L)).willReturn(menus);

		StoreMenuListResponse res = menuService.getStoreMenus(storeId);

		assertEquals(2, res.menus().size());
		assertEquals("메뉴 이름1", res.menus().get(0).name());
		assertEquals("메뉴 이름2", res.menus().get(1).name());
		assertEquals("메뉴 옵션 이름1", res.menus().get(0).options().get(0).name());
		assertEquals("메뉴 옵션 이름2", res.menus().get(1).options().get(0).name());
	}

	@Test
	void getStoreMenus_whenStoreNotFound_shouldThrowException() {
		Long storeId = 1L;

		given(storeRepository.existsById(storeId)).willReturn(false);

		BizException exception = assertThrows(BizException.class, () -> menuService.getStoreMenus(storeId));
		assertEquals(StoreErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void updateMenu_shouldSucceed() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;

		Menu mockMenu = createMockMenu(userId, storeId, menuId);

		MenuUpdateRequest req
			= new MenuUpdateRequest("수정된 이름", 20000, "https://image.com/image_url", true);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.of(mockMenu));

		menuService.updateMenu(userId, storeId, menuId, req);

		assertEquals("수정된 이름", mockMenu.getName());
		assertEquals(20000, mockMenu.getPrice());
		assertEquals("https://image.com/image_url", mockMenu.getImageUrl());
		assertTrue(mockMenu.isSoldOut());
	}

	@Test
	void updateMenu_whenMenuNotFound_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;

		MenuUpdateRequest req
			= new MenuUpdateRequest("수정된 이름", 20000, "https://image.com/image_url", true);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.empty());

		BizException exception = assertThrows(BizException.class,
			() -> menuService.updateMenu(userId, storeId, menuId, req));
		assertEquals(MenuErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void updateMenu_whenStoreInvalid_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;

		Menu mockMenu = createMockMenu(userId, storeId, menuId);

		MenuUpdateRequest req
			= new MenuUpdateRequest("수정된 이름", 20000, "https://image.com/image_url", true);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.of(mockMenu));

		BizException exception = assertThrows(BizException.class,
			() -> menuService.updateMenu(userId, 2L, menuId, req));
		assertEquals(MenuErrorCode.MENU_NOT_BELONG_TO_STORE, exception.getErrorCode());
	}

	@Test
	void updateMenu_whenStoreOwnerInvalid_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;

		Menu mockMenu = createMockMenu(userId, storeId, menuId);

		MenuUpdateRequest req
			= new MenuUpdateRequest("수정된 이름", 20000, "https://image.com/image_url", true);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.of(mockMenu));

		BizException exception = assertThrows(BizException.class,
			() -> menuService.updateMenu(2L, storeId, menuId, req));
		assertEquals(StoreErrorCode.STORE_OWNER_MISMATCH, exception.getErrorCode());
	}

	Menu createMockMenu(Long userId, Long storeId, Long menuId) {
		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);

		Store store = Store.builder().user(user).build();
		ReflectionTestUtils.setField(store, "id", storeId);

		Menu menu = Menu.builder().store(store).build();
		ReflectionTestUtils.setField(menu, "id", menuId);
		return menu;
	}
}
