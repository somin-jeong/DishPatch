package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;
import static com.example.dishpatch.domain.user.exception.UserErrorCode.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
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

	public StoreCreateResponse createStore(User user, StoreCreateRequest request) {
		// TODO: 사용자 role이 사장인지 확인

		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

		int storeCount = storeRepository.countByUserIdAndDeletedDateIsNull(1L); // TODO: user.getId()
		if (storeCount >= 3) {
			throw new BizException(STORE_OWN_LIMIT_EXCEEDED);
		}

		Store store = Store.of(request, user, category);

		storeRepository.save(store);

		return StoreCreateResponse.from(store);
	}

	public void dibStore(User user, Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));

		if (dibRepository.existsByUserIdAndStoreId(user.getId(), storeId)) {
			throw new BizException(ALREADY_DIB_STORE);
		}

		Dib dib = Dib.of(user, store);

		dibRepository.save(dib);
	}

	public void undibStore(User user, Long storeId) {
		if (!storeRepository.existsById(storeId)) {
			throw new BizException(STORE_NOT_FOUND);
		}

		Dib dib = dibRepository.findByUserIdAndStoreId(user.getId(), storeId)
			.orElseThrow(() -> new BizException(UNDIB_STORE));

		dibRepository.delete(dib);
	}

	@Modifying(clearAutomatically = true)
	public void deleteStore(Long userId, Long storeId) {
		userRepository.findById(userId).ifPresent(user -> {
			if (user.getRole() != UserRole.CEO) {
				throw new BizException(USER_ROLE_NOT_CEO);
			}
		});

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND));

		if (!Objects.equals(store.getUser().getId(), userId)) {
			throw new BizException(STORE_OWNER_MISMATCH);
		}

		LocalDateTime now = LocalDateTime.now();

		reviewRepository.deleteAllByStoreId(storeId);
		ceoReviewRepository.deleteAllByStoreId(storeId, now);

		menuRepository.bulkSoftDeleteByStoreId(storeId, now);
		menuOptionRepository.bulkSoftDeleteByStoreId(storeId, now);

		dibRepository.deleteAllByStoreId(storeId);
		cartRepository.deleteAllByStoreId(storeId);

		storeRepository.findById(storeId)
			.orElseThrow(() -> new BizException(STORE_NOT_FOUND)).softDelete();
	}

}
