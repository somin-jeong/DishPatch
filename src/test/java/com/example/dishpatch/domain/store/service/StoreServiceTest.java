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
import com.example.dishpatch.infra.db.store.entity.Dib;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
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

	@Mock
	private DibRepository dibRepository;

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

	@Test
	void dibStore_shouldSucceed() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);

		when(storeRepository.findById(user.getId())).thenReturn(Optional.of(store));
		when(dibRepository.existsByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(false);

		ArgumentCaptor<Dib> dibCaptor = ArgumentCaptor.forClass(Dib.class);

		// when
		storeService.dibStore(user, store.getId());

		// then
		verify(dibRepository, times(1)).save(dibCaptor.capture());
		Dib savedDib = dibCaptor.getValue();
		assertThat(savedDib.getUser()).isEqualTo(user);
		assertThat(savedDib.getStore()).isEqualTo(store);
	}

	@Test
	void dibStore_whenNotFoundStore_shouldThrowException() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);

		when(storeRepository.findById(user.getId())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.dibStore(user, store.getId()));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void dibStore_whenAlreadyDibStore_shouldThrowException() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);

		when(storeRepository.findById(user.getId())).thenReturn(Optional.of(store));
		when(dibRepository.existsByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(true);

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.dibStore(user, store.getId()));

		assertThat(ALREADY_DIB_STORE.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void undibStore_shouldSucceed() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);
		Dib dib = Dib.of(user, store);

		when(storeRepository.existsById(store.getId())).thenReturn(true);
		when(dibRepository.findByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.of(dib));

		ArgumentCaptor<Dib> dibCaptor = ArgumentCaptor.forClass(Dib.class);

		// when
		storeService.undibStore(user, store.getId());

		// then
		verify(dibRepository, times(1)).delete(dibCaptor.capture());
		Dib deletedDib = dibCaptor.getValue();
		assertThat(deletedDib.getUser()).isEqualTo(user);
		assertThat(deletedDib.getStore()).isEqualTo(store);
	}

	@Test
	void undibStore_whenNotFoundStore_shouldThrowException() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);

		when(storeRepository.existsById(store.getId())).thenReturn(false);

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.undibStore(user, store.getId()));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void undibStore_whenUndibStore_shouldThrowException() {
		// given
		User user = mock(User.class);
		Store store = mock(Store.class);

		when(storeRepository.existsById(store.getId())).thenReturn(true);
		when(dibRepository.findByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.undibStore(user, store.getId()));

		assertThat(UNDIB_STORE.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}
}
