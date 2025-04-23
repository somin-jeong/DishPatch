package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
	@InjectMocks
	private StoreService storeService;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Test
	void createStore_shouldSucceed() {
		// given
		User user = mock(User.class);
		Category category = mock(Category.class);
		StoreCreateRequest request = mock(StoreCreateRequest.class);

		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(storeRepository.countByUserIdAndDeletedDateIsNull(any())).thenReturn(2);

		ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);

		// when
		StoreCreateResponse response = storeService.createStore(user, request);

		// then
		verify(storeRepository, times(1)).save(storeCaptor.capture());
		Store savedStore = storeCaptor.getValue();
		assertThat(savedStore.getUser()).isEqualTo(user);
		assertThat(savedStore.getCategory()).isEqualTo(category);

		assertThat(response.storeId()).isEqualTo(savedStore.getId());
	}

	@Test
	void createMenu_whenStoreOwnLimitExceed_shouldThrowException() {
		// given
		User user = mock(User.class);
		Category category = mock(Category.class);
		StoreCreateRequest request = mock(StoreCreateRequest.class);

		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(storeRepository.countByUserIdAndDeletedDateIsNull(any())).thenReturn(3); // 최대 초과

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.createStore(user, request));

		assertThat(STORE_OWN_LIMIT_EXCEEDED.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void createMenu_whenNotFoundCategory_shouldThrowException() {
		// given
		User user = mock(User.class);
		StoreCreateRequest request = mock(StoreCreateRequest.class);

		when(categoryRepository.findById(any())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.createStore(user, request));

		assertThat(CATEGORY_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}
}