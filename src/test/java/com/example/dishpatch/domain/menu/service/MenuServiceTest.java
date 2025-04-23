package com.example.dishpatch.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dishpatch.api.menu.request.MenuCreateRequest;
import com.example.dishpatch.api.menu.response.MenuCreateResponse;
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
}
