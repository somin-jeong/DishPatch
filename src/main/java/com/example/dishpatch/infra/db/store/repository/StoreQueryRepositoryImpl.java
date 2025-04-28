package com.example.dishpatch.infra.db.store.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.dishpatch.api.store.response.StoreResponse;
import com.example.dishpatch.api.store.response.StoreSearchResponse;
import com.example.dishpatch.infra.db.menu.entity.QMenu;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.entity.QOrder;
import com.example.dishpatch.infra.db.store.entity.QStore;
import com.example.dishpatch.infra.db.store.enums.SortType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QStore qStore = QStore.store;
	private final QOrder qOrder = QOrder.order;
	private final QMenu qMenu = QMenu.menu;

	@Override
	public Slice<StoreResponse> findAllBySortType(SortType sortType, Long cursorId, int size) {
		List<StoreResponse> stores;

		if (sortType != null && "ORDER_COUNT".equals(sortType.name())) {
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

	@Override
	public Slice<StoreSearchResponse> findAllByKeyword(String keyword, Long cursorId, int size) {
		List<StoreSearchResponse> stores = queryFactory
			.select(Projections.constructor(StoreSearchResponse.class,
				qStore.id,
				qStore.name,
				qStore.image,
				qStore.deliveryFee,
				qStore.minDeliveryPrice,
				qStore.rating,
				qStore.reviewCount
			))
			.from(qStore)
			.leftJoin(qMenu).on(qMenu.store.id.eq(qStore.id))
			.where(
				qStore.deletedDate.isNull(),
				qStore.name.contains(keyword).or(qMenu.name.contains(keyword)),
				ltCursorId(cursorId)
			)
			.groupBy(qStore.id)
			.orderBy(qStore.isAdvertised.desc(), qStore.id.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = stores.size() > size;

		if (hasNext) {
			stores = stores.subList(0, size); // 1개 더 가져온 것 잘라냄
		}

		if (stores.isEmpty()) {
			return new SliceImpl<>(Collections.emptyList(), PageRequest.of(0, size), hasNext);
		}

		List<Long> storeIds = stores.stream()
			.map(StoreSearchResponse::getId)
			.toList();

		List<Tuple> menuTuples = queryFactory
			.select(qMenu.store.id, qMenu.name)
			.from(qMenu)
			.where(
				qMenu.store.id.in(storeIds),
				qMenu.deletedDate.isNull()
			)
			.orderBy(
				new CaseBuilder()
					.when(qMenu.name.eq(keyword)).then(2)
					.when(qMenu.name.contains(keyword)).then(1)
					.otherwise(0)
					.desc(),
				qMenu.name.asc()
			)
			.fetch();

		Map<Long, List<String>> storeIdToMenuNames = menuTuples.stream()
			.collect(Collectors.groupingBy(
				tuple -> tuple.get(qMenu.store.id),
				Collectors.mapping(tuple -> tuple.get(qMenu.name), Collectors.toList())
			));

		for (StoreSearchResponse store : stores) {
			List<String> menuNames = storeIdToMenuNames.getOrDefault(store.getId(), List.of());
			store.setMenuNameList(menuNames);
		}

		return new SliceImpl<>(stores, PageRequest.of(0, size), hasNext);
	}

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
				qStore.deletedDate.isNull(),
				categoryEq(categoryId),
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

	private List<OrderSpecifier<?>> getSortOrders(SortType sortType) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		orders.add(qStore.isAdvertised.desc());

		if (sortType != null) {
			switch (sortType.name()) {
				case "DIB" -> {
					orders.add(qStore.dibCount.desc());
				}
				case "RATING" -> {
					orders.add(qStore.rating.desc());
				}
			}
		}

		orders.add(qStore.id.desc());
		return orders;
	}

	private List<OrderSpecifier<?>> getOrderCountSortOrders() {
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
