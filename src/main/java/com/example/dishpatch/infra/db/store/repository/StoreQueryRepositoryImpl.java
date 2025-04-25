package com.example.dishpatch.infra.db.store.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.infra.db.store.entity.QStore;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QStore qStore = QStore.store;

	@Override
	public Slice<StoreResponse> findAllByCategoryId(Long categoryId, Long cursorId, int size) {
		List<StoreResponse> stores = queryFactory
			.select(Projections.constructor(StoreResponse.class,
				qStore.id,
				qStore.name,
				qStore.image,
				qStore.deliveryFee,
				qStore.minDeliveryPrice,
				qStore.rating,
				qStore.reviewCount
			))
			.from(qStore)
			.where(
				categoryEq(categoryId),
				qStore.deletedDate.isNull(),
				ltCursorId(cursorId)
			)
			.orderBy(qStore.isAdvertised.desc(), qStore.id.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = stores.size() > size;

		if (hasNext) {
			stores.remove(size);
		}

		return new SliceImpl<>(stores, PageRequest.of(0, size), hasNext);
	}

	private BooleanExpression categoryEq(Long categoryId) {
		return categoryId == null ? null : qStore.category.id.eq(categoryId);
	}

	private BooleanExpression ltCursorId(Long cursorId) {
		return cursorId == null ? null : qStore.id.lt(cursorId);
	}

}
