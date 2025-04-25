package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;
import static com.example.dishpatch.domain.user.exception.UserErrorCode.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.request.StoreUpdateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.response.pagination.SliceResponse;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.cart.repository.CartRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Dib;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.enums.SortType;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.DibRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserStatus;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreService {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final DibRepository dibRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final CeoReviewRepository ceoReviewRepository;
	private final MenuRepository menuRepository;
	private final MenuOptionRepository menuOptionRepository;
	private final CartRepository cartRepository;

	@Transactional
	public StoreCreateResponse createStore(UserAuth userAuth, StoreCreateRequest request) {
		User user = userRepository.findByIdAndStatus(userAuth.getId(), UserStatus.ACTIVE)
			.orElseThrow(() -> new BizException(INVALID_ID));

		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

		int storeCount = storeRepository.countByUserIdAndDeletedDateIsNull(userAuth.getId());
		if (storeCount >= 3) {
			throw new BizException(STORE_OWN_LIMIT_EXCEEDED);
		}

		Store store = Store.of(request, user, category);

		storeRepository.save(store);

		return StoreCreateResponse.from(store);
	}

	@Transactional
	public void updateStore(UserAuth userAuth, Long storeId, StoreUpdateRequest request) {
		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

		Store store = storeRepository.findByIdAndDeletedDateIsNull(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));

		if (!Objects.equals(store.getUser().getId(), userAuth.getId())) {
			throw new BizException(STORE_OWNER_MISMATCH);
		}

		store.update(request, category);
	}

	@Transactional
	public void dibStore(UserAuth userAuth, Long storeId) {
		User user = userRepository.findByIdAndStatus(userAuth.getId(), UserStatus.ACTIVE)
			.orElseThrow(() -> new BizException(INVALID_ID));

		Store store = storeRepository.findByIdAndDeletedDateIsNull(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));
		store.plusDib();

		if (dibRepository.existsByUserIdAndStoreId(userAuth.getId(), storeId)) {
			throw new BizException(ALREADY_DIB_STORE);
		}

		Dib dib = Dib.of(user, store);

		dibRepository.save(dib);
	}

	@Transactional
	public void undibStore(UserAuth userAuth, Long storeId) {
		Store store = storeRepository.findByIdAndDeletedDateIsNull(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));
		store.minusDib();

		Dib dib = dibRepository.findByUserIdAndStoreId(userAuth.getId(), storeId)
			.orElseThrow(() -> new BizException(UNDIB_STORE));

		dibRepository.delete(dib);
	}

	@Transactional
	public void deleteStore(UserAuth userAuth, Long storeId) {
		Store store = storeRepository.findByIdAndDeletedDateIsNull(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));

		if (!Objects.equals(store.getUser().getId(), userAuth.getId())) {
			throw new BizException(STORE_OWNER_MISMATCH);
		}

		store.softDelete();

		reviewRepository.deleteAllByStoreId(storeId);
		ceoReviewRepository.deleteAllByStoreId(storeId);

		menuRepository.bulkSoftDeleteByStoreId(storeId, LocalDateTime.now());
		menuOptionRepository.bulkSoftDeleteByStoreId(storeId, LocalDateTime.now());

		dibRepository.deleteAllByStoreId(storeId);
		cartRepository.deleteAllByStoreId(storeId);
	}

	@Transactional(readOnly = true)
	public SliceResponse<StoreResponse> getStore(SortType sortType, Long categoryId, Long cursorId, int size) {
		if (categoryId != null) {
			categoryRepository.findById(categoryId)
				.orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));
		}

		return SliceResponse.from(storeRepository.findAllByCategoryId(sortType, categoryId, cursorId, size));
	}

}
