package com.example.dishpatch.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

	private static final Long USER_ID = 1L;
	private static final Long STORE_ID = 1L;
	private static final Long MENU_ID = 1L;
	private static final Long MENU_OPTION_ID = 1L;

	private Menu menu;

	@BeforeEach
	void setUp() {
		menu = createTestMenu(USER_ID, STORE_ID, MENU_ID);
	}

	@Test
	void addMenuOption_shouldSucceed() {
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(MENU_ID)).willReturn(Optional.of(menu));

		MenuOptionAddResponse res = menuOptionService.addMenuOption(USER_ID, MENU_ID, req);

		verify(menuOptionRepository, times(1)).save(any(MenuOption.class));
		assertEquals("메뉴 옵션 이름", res.name());
		assertEquals(10000, res.price());
	}

	@Test
	void addMenuOption_whenMenuNotFound_shouldThrowException() {
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(MENU_ID)).willReturn(Optional.empty());

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.addMenuOption(USER_ID, MENU_ID, req));
		assertEquals(MenuErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void addMenuOption_whenStoreOwnerMismatch_shouldThrowException() {
		MenuOptionAddRequest req = new MenuOptionAddRequest("메뉴 옵션 이름", 10000);

		given(menuRepository.findByMenuId(MENU_ID)).willReturn(Optional.of(menu));

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.addMenuOption(2L, MENU_ID, req));
		assertEquals(StoreErrorCode.STORE_OWNER_MISMATCH, exception.getErrorCode());
	}

	@Test
	void updateMenuOption_shouldSucceed() {
		MenuOptionUpdateRequest req = new MenuOptionUpdateRequest("메뉴 옵션 이름 수정", 20000, true);
		MenuOption menuOption = mock(MenuOption.class);

		given(menuOption.getMenu()).willReturn(menu);
		given(menuOptionRepository.findByIdWithMenuAndStore(MENU_OPTION_ID)).willReturn(Optional.of(menuOption));

		menuOptionService.updateMenuOption(USER_ID, MENU_ID, MENU_OPTION_ID, req);

		verify(menuOption, times(1))
			.update(eq("메뉴 옵션 이름 수정"), eq(20000), eq(true));
	}

	@Test
	void updateMenuOption_whenMenuOptionNotFound_shouldThrowException() {
		MenuOptionUpdateRequest req = new MenuOptionUpdateRequest("메뉴 옵션 이름 수정", 20000, true);

		given(menuOptionRepository.findByIdWithMenuAndStore(MENU_OPTION_ID)).willReturn(Optional.empty());

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.updateMenuOption(USER_ID, MENU_ID, MENU_OPTION_ID, req));
		assertEquals(MenuOptionErrorCode.MENU_OPTION_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void updateMenuOption_whenMenuOptionNotBelongToMenu_shouldThrowException() {
		MenuOptionUpdateRequest req = new MenuOptionUpdateRequest("메뉴 옵션 이름 수정", 20000, true);
		MenuOption menuOption = mock(MenuOption.class);

		given(menuOption.getMenu()).willReturn(menu);
		given(menuOptionRepository.findByIdWithMenuAndStore(MENU_OPTION_ID)).willReturn(Optional.of(menuOption));

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.updateMenuOption(USER_ID, 2L, MENU_OPTION_ID, req));
		assertEquals(MenuOptionErrorCode.MENU_OPTION_NOR_BELONG_TO_MENU, exception.getErrorCode());
	}

	@Test
	void updateMenuOption_whenStoreOwnerMismatch_shouldThrowException() {
		MenuOptionUpdateRequest req = new MenuOptionUpdateRequest("메뉴 옵션 이름 수정", 20000, true);
		MenuOption menuOption = mock(MenuOption.class);

		given(menuOption.getMenu()).willReturn(menu);
		given(menuOptionRepository.findByIdWithMenuAndStore(MENU_OPTION_ID)).willReturn(Optional.of(menuOption));

		BizException exception = assertThrows(BizException.class,
			() -> menuOptionService.updateMenuOption(2L, MENU_ID, MENU_OPTION_ID, req));
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
