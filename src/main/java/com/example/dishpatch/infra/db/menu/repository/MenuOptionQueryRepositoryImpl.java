package com.example.dishpatch.infra.db.menu.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.entity.QMenuOption;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MenuOptionQueryRepositoryImpl implements MenuOptionQueryRepository {

	private final JPAQueryFactory queryFactory;

	private final QMenuOption qMenuOption = QMenuOption.menuOption;

	@Override
	public Optional<MenuOption> findByIdWithMenuAndStore(Long id) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(qMenuOption)
				.leftJoin(qMenuOption.menu).fetchJoin()
				.leftJoin(qMenuOption.menu.store).fetchJoin()
				.where(
					qMenuOption.id.eq(id),
					qMenuOption.deleted.isFalse()
				)
				.fetchOne()
		);
	}
}
