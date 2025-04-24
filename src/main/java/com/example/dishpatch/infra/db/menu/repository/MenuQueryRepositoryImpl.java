package com.example.dishpatch.infra.db.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MenuQueryRepositoryImpl implements MenuQueryRepository {

	private final JPAQueryFactory queryFactory;

	private final QMenu qMenu = QMenu.menu;

	@Override
	public List<Menu> findAllByStoreId(Long storeId) {
		return queryFactory
			.selectFrom(qMenu)
			.leftJoin(qMenu.options).fetchJoin()
			.where(
				qMenu.store.id.eq(storeId),
				qMenu.deleted.isFalse()
			)
			.fetch();
	}

	@Override
	public Optional<Menu> findByMenuId(Long menuId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(qMenu)
				.leftJoin(qMenu.store).fetchJoin()
				.where(
					qMenu.id.eq(menuId),
					qMenu.deleted.isFalse()
				)
				.fetchOne()
		);
	}
}
