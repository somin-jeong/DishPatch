package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.response.StoreCreateResponse;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreService {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;

	public StoreCreateResponse createStore(User user, StoreCreateRequest request) {
		// TODO: 사용자 role이 사장인지 확인

		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

		int storeCount = storeRepository.countByUserIdAndDeletedDateIsNull(1L); // TODO: user.getId()
		if (storeCount >= 3) {
			throw new BizException(STORE_OWN_LIMIT_EXCEEDED);
		}

		Store store = Store.builder()
			.name(request.name())
			.address(request.address())
			.phone(request.phone())
			.image(request.imageUrl())
			.introduction(request.introduction())
			.deliveryFee(request.deliveryFee())
			.minDeliveryPrice(request.minDeliveryPrice())
			.isAdvertised(request.isAdvertised())
			.openTime(request.getOpenTime())
			.closeTime(request.getCloseTime())
			.user(user)
			.category(category)
			.build();

		storeRepository.save(store);

		return StoreCreateResponse.from(store);
	}
}
