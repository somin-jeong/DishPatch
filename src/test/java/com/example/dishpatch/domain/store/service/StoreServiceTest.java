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
import com.example.dishpatch.api.store.request.StoreUpdateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Dib;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.enums.SortType;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

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
	@Mock
	private UserRepository userRepository;

	private final Long userId = 1L;
	private final Long storeId = 3L;
	private final Long categoryId = 5L;
	private final Long ownerId = 10L;
	private final User user = mock(User.class);
	private final UserAuth userAuth = mock(UserAuth.class);
	private final Category category = mock(Category.class);
	private final Store store = mock(Store.class);
	private final User owner = mock(User.class);

	@Test
	void createStore_shouldSucceed() {
		// given
		StoreCreateRequest request = mock(StoreCreateRequest.class);
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(storeRepository.countByUserIdAndDeletedDateIsNull(any())).thenReturn(2);

		ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);

		// when
		StoreCreateResponse response = storeService.createStore(userAuth, request);

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
		StoreCreateRequest request = mock(StoreCreateRequest.class);
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
		when(storeRepository.countByUserIdAndDeletedDateIsNull(any())).thenReturn(3); // 최대 초과

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.createStore(userAuth, request));

		assertThat(STORE_OWN_LIMIT_EXCEEDED.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void createMenu_whenNotFoundCategory_shouldThrowException() {
		// given
		StoreCreateRequest request = mock(StoreCreateRequest.class);
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(any())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.createStore(userAuth, request));

		assertThat(CATEGORY_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void dibStore_shouldSucceed() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));
		when(dibRepository.existsByUserIdAndStoreId(userId, storeId)).thenReturn(false);

		ArgumentCaptor<Dib> dibCaptor = ArgumentCaptor.forClass(Dib.class);

		// when
		storeService.dibStore(userAuth, storeId);

		// then
		verify(dibRepository, times(1)).save(dibCaptor.capture());
		Dib savedDib = dibCaptor.getValue();
		assertThat(savedDib.getUser()).isEqualTo(user);
		assertThat(savedDib.getStore()).isEqualTo(store);
	}

	@Test
	void dibStore_whenNotFoundStore_shouldThrowException() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.dibStore(userAuth, storeId));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void dibStore_whenAlreadyDibStore_shouldThrowException() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(userRepository.findByIdAndDeletedDateIsNull(userId)).thenReturn(Optional.of(user));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));
		when(dibRepository.existsByUserIdAndStoreId(userId, storeId)).thenReturn(true);

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.dibStore(userAuth, storeId));

		assertThat(ALREADY_DIB_STORE.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void undibStore_shouldSucceed() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));
		Dib dib = Dib.of(user, store);
		when(dibRepository.findByUserIdAndStoreId(userId, storeId)).thenReturn(Optional.of(dib));

		ArgumentCaptor<Dib> dibCaptor = ArgumentCaptor.forClass(Dib.class);

		// when
		storeService.undibStore(userAuth, storeId);

		// then
		verify(dibRepository, times(1)).delete(dibCaptor.capture());
		Dib deletedDib = dibCaptor.getValue();
		assertThat(deletedDib.getUser()).isEqualTo(user);
		assertThat(deletedDib.getStore()).isEqualTo(store);
	}

	@Test
	void undibStore_whenNotFoundStore_shouldThrowException() {
		// given
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.undibStore(userAuth, storeId));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void undibStore_whenUndibStore_shouldThrowException() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));
		when(dibRepository.findByUserIdAndStoreId(userId, storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.undibStore(userAuth, storeId));

		assertThat(UNDIB_STORE.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_shouldSucceed() {
		// given
		when(user.getId()).thenReturn(userId);
		when(store.getUser()).thenReturn(user);
		when(userAuth.getId()).thenReturn(userId);

		StoreUpdateRequest request = mock(StoreUpdateRequest.class);
		when(request.categoryId()).thenReturn(categoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));

		// when
		storeService.updateStore(userAuth, storeId, request);

		// then
		verify(store, times(1)).update(request, category);
	}

	@Test
	void updateStore_whenNotFoundCategory_shouldThrowException() {
		// given
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);
		when(request.categoryId()).thenReturn(categoryId);
		when(categoryRepository.findById(any())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userAuth, storeId, request));

		assertThat(CATEGORY_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_whenNotFoundStore_shouldThrowException() {
		// given
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);
		when(request.categoryId()).thenReturn(categoryId);

		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userAuth, storeId, request));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_whenStoreOwnerMismatch_shouldThrowException() {
		// given
		when(user.getId()).thenReturn(ownerId);
		when(store.getUser()).thenReturn(user);
		when(userAuth.getId()).thenReturn(userId);

		StoreUpdateRequest request = mock(StoreUpdateRequest.class);
		when(request.categoryId()).thenReturn(categoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userAuth, storeId, request));

		assertThat(STORE_OWNER_MISMATCH.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void deleteStore_whenStoreNotFound_shouldThrowException() {
		// given
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.deleteStore(userAuth, storeId));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void deleteStore_whenStoreOwnerMismatch_shouldThrowException() {
		// given
		when(userAuth.getId()).thenReturn(userId);
		when(owner.getId()).thenReturn(ownerId);

		Store store = mock(Store.class);
		when(store.getUser()).thenReturn(owner);

		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.of(store));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.deleteStore(userAuth, storeId));

		assertThat(STORE_OWNER_MISMATCH.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void getStore_whenNotFoundCategory_shouldThrowException() {
		// given
		SortType sortType = SortType.RATING;

		when(categoryRepository.findById(any())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.getStore(categoryId, 1L, 10));

		assertThat(CATEGORY_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void getStoreInfo_whenStoreNotFound_shouldThrowException() {
		// given
		when(storeRepository.findByIdAndDeletedDateIsNull(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.getStoreInfo(storeId));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

}
