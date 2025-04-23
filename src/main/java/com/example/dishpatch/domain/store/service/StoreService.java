package com.example.dishpatch.domain.store.service;

import static com.example.dishpatch.domain.store.exception.StoreErrorCode.*;

import org.springframework.stereotype.Service;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreService {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final DibRepository dibRepository;

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
}
