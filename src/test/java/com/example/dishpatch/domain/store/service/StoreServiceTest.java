package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;
import static com.example.dishpatch.domain.user.exception.UserErrorCode.*;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.request.StoreUpdateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.cart.repository.CartRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Dib;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserRole;
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
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private CeoReviewRepository ceoReviewRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuOptionRepository menuOptionRepository;
	@Mock
	private CartRepository cartRepository;

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

	@Test
	void updateStore_shouldSucceed() {
		// given
		Long userId = 1L;
		Long storeId = 10L;
		Long categoryId = 3L;

		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "role", UserRole.CEO);

		Category category = new Category();
		ReflectionTestUtils.setField(category, "id", categoryId);

		Store store = new Store();
		ReflectionTestUtils.setField(store, "id", storeId);
		ReflectionTestUtils.setField(store, "user", user);

		StoreUpdateRequest request = new StoreUpdateRequest("새이름", "주소", "전화번호", "이미지", 1000, "소개", 12000, false,
			"10:00", "22:00", categoryId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when
		storeService.updateStore(userId, storeId, request);

		// then
		assertThat(store.getName()).isEqualTo("새이름");
		assertThat(store.getUser().getId()).isEqualTo(userId);
		assertThat(store.getCategory().getId()).isEqualTo(categoryId);
	}

	@Test
	void updateStore_whenNotFoundCategory_shouldThrowException() {
		Long storeId = 1L;
		Long userId = 1L;
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);

		User user = new User();
		ReflectionTestUtils.setField(user, "role", UserRole.CEO);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(any())).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userId, storeId, request));

		assertThat(CATEGORY_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_whenNotFoundStore_shouldThrowException() {
		Long storeId = 1L;
		Long userId = 1L;
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);

		User user = new User();
		ReflectionTestUtils.setField(user, "role", UserRole.CEO);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
		when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userId, storeId, request));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_whenUserRoleNotCeo_shouldThrowException() {
		// given
		Long storeId = 1L;
		Long userId = 3L;
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);

		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "role", UserRole.USER);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userId, storeId, request));

		assertThat(USER_ROLE_NOT_CEO.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void updateStore_whenStoreOwnerMismatch_shouldThrowException() {
		// given
		Long storeId = 1L;
		Long userId = 3L;
		Long ownerId = 10L;
		StoreUpdateRequest request = mock(StoreUpdateRequest.class);

		User owner = new User();
		ReflectionTestUtils.setField(owner, "id", ownerId);
		ReflectionTestUtils.setField(owner, "role", UserRole.CEO);

		User user = new User();
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "role", UserRole.CEO);

		Store store = new Store();
		ReflectionTestUtils.setField(store, "id", storeId);
		ReflectionTestUtils.setField(store, "user", owner);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.updateStore(userId, storeId, request));

		assertThat(STORE_OWNER_MISMATCH.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void deleteStore_whenUserRoleNotCeo_shouldThrowException() {
		// given
		Long userId = 1L;
		User user = mock(User.class);
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "role", UserRole.USER);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.deleteStore(userId, 10L));

		assertThat(USER_ROLE_NOT_CEO.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void deleteStore_whenStoreNotFound_shouldThrowException() {
		// given
		Long userId = 1L;
		Long storeId = 10L;

		User user = mock(User.class);
		when(user.getRole()).thenReturn(UserRole.CEO);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.deleteStore(userId, storeId));

		assertThat(STORE_NOT_FOUND.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

	@Test
	void deleteStore_whenStoreOwnerMismatch_shouldThrowException() {
		// given
		Long userId = 1L;
		Long ownerId = 3L;
		Long storeId = 10L;

		User owner = mock(User.class);
		when(owner.getId()).thenReturn(ownerId);

		Store store = mock(Store.class);
		when(store.getUser()).thenReturn(owner);

		User user = mock(User.class);
		when(user.getRole()).thenReturn(UserRole.CEO);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when & then
		BizException exception = assertThrows(BizException.class,
			() -> storeService.deleteStore(userId, storeId));

		assertThat(STORE_OWNER_MISMATCH.getMessage()).isEqualTo(exception.getErrorCode().getMessage());
	}

}
