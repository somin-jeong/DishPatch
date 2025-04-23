package com.example.dishpatch.infra.db.menu.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MenuQueryRepositoryImpl implements MenuQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Menu> findAllByStoreId(Long storeId) {
		QMenu menu = QMenu.menu;

		return queryFactory
			.selectFrom(menu)
			.leftJoin(menu.options).fetchJoin()
			.where(
				menu.store.id.eq(storeId),
				menu.deleted.isFalse()
			)
			.fetch();
	}
}
