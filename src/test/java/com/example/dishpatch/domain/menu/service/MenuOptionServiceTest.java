package com.example.dishpatch.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.menu.request.MenuOptionAddRequest;
import com.example.dishpatch.api.menu.response.MenuOptionAddResponse;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;

@ExtendWith(MockitoExtension.class)
class MenuOptionServiceTest {

	@InjectMocks
	private MenuOptionService menuOptionService;

	@Mock
	private MenuOptionRepository menuOptionRepository;

	@Mock
	private MenuRepository menuRepository;

	@Test
	void addMenuOption_shouldSucceed() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;
		Menu menu = createTestMenu(userId, storeId, menuId);
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.of(menu));

		MenuOptionAddResponse res = menuOptionService.addMenuOption(userId, menuId, req);

		verify(menuOptionRepository, times(1)).save(any(MenuOption.class));
		assertEquals("메뉴 옵션 이름", res.name());
		assertEquals(10000, res.price());
	}

	@Test
	void addMenuOption_whenMenuNotFound_shouldThrowException() {
		Long userId = 1L;
		Long menuId = 1L;
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.empty());

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.addMenuOption(userId, menuId, req));
		assertEquals(MenuErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void addMenuOption_whenNotStoreOwner_shouldThrowException() {
		Long userId = 1L;
		Long storeId = 1L;
		Long menuId = 1L;
		Menu menu = createTestMenu(userId, storeId, menuId);
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(menuId)).willReturn(Optional.of(menu));

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.addMenuOption(2L, menuId, req));
		assertEquals(StoreErrorCode.STORE_OWNER_MISMATCH, exception.getErrorCode());
	}

	Menu createTestMenu(Long userId, Long storeId, Long menuId) {
		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);

		Store store = Store.builder().user(user).build();
		ReflectionTestUtils.setField(store, "id", storeId);

		Menu menu = Menu.builder().store(store).build();
		ReflectionTestUtils.setField(menu, "id", menuId);
		return menu;
	}
}
