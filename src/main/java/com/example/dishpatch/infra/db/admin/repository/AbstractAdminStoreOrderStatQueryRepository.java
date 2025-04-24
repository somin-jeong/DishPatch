package com.example.dishpatch.infra.db.admin.repository;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class AbstractAdminStoreOrderStatQueryRepository<T, Q extends EntityPathBase<T>> {

	protected final JPAQueryFactory queryFactory;
	protected final Q q;
	protected final DatePath<LocalDate> datePath;

	public AbstractAdminStoreOrderStatQueryRepository(JPAQueryFactory queryFactory, Q q, DatePath<LocalDate> datePath) {
		this.queryFactory = queryFactory;
		this.q = q;
		this.datePath = datePath;
	}

	public List<T> findAllByDateRange(LocalDate from, LocalDate to) {
		return queryFactory
			.selectFrom(q)
			.where(
				datePath.goe(from),
				datePath.lt(to)
			)
			.fetch();
	}

}
