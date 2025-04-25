package com.example.dishpatch.infra.db.store.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.entity.QOrder;
import com.example.dishpatch.infra.db.store.entity.QStore;
import com.example.dishpatch.infra.db.store.enums.SortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QStore qStore = QStore.store;

	@Override
	public Slice<StoreResponse> findAllByCategoryId(SortType sortType, Long categoryId, Long cursorId, int size) {
		List<StoreResponse> stores;

		if ("ORDER_COUNT".equals(sortType.name())) {
			QOrder qOrder = QOrder.order;

			stores = queryFactory
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
				.leftJoin(qOrder).on(qOrder.store.id.eq(qStore.id))
				.where(
					categoryEq(categoryId),
					qStore.deletedDate.isNull(),
					ltCursorId(cursorId)
				)
				.groupBy(qStore.id)
				.orderBy(getOrderCountSortOrders().toArray(OrderSpecifier[]::new))
				.limit(size + 1)
				.fetch();

		} else {
			stores = queryFactory
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
				.orderBy(getSortOrders(sortType).toArray(OrderSpecifier[]::new))
				.limit(size + 1)
				.fetch();
		}

		boolean hasNext = stores.size() > size;

		if (hasNext) {
			stores.remove(size);
		}

		return new SliceImpl<>(stores, PageRequest.of(0, size), hasNext);
	}

	private List<OrderSpecifier<?>> getSortOrders(SortType sortType) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		orders.add(qStore.isAdvertised.desc());

		switch (sortType.name()) {
			case "DIB" -> {
				orders.add(qStore.dibCount.desc());
			}
			case "RATING" -> {
				orders.add(qStore.rating.desc());
			}

		}
		orders.add(qStore.id.desc());
		return orders;
	}

	private List<OrderSpecifier<?>> getOrderCountSortOrders() {
		QOrder qOrder = QOrder.order;

		OrderSpecifier<Long> orderCountDesc = Expressions.numberTemplate(
			Long.class,
			"count(case when {0} = {1} then 1 end)",
			qOrder.status,
			OrderStatus.FINISHED
		).desc();

		return List.of(qStore.isAdvertised.desc(), orderCountDesc, qStore.id.desc());
	}

	private BooleanExpression categoryEq(Long categoryId) {
		return categoryId == null ? null : qStore.category.id.eq(categoryId);
	}

	private BooleanExpression ltCursorId(Long cursorId) {
		return cursorId == null ? null : qStore.id.lt(cursorId);
	}

}
