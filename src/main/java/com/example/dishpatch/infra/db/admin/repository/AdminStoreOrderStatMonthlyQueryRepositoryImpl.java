package com.example.dishpatch.infra.db.admin.repository;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.entity.QAdminStoreOrderStatMonthly;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class AdminStoreOrderStatMonthlyQueryRepositoryImpl
	extends AbstractAdminStoreOrderStatQueryRepository<AdminStoreOrderStatMonthly, QAdminStoreOrderStatMonthly>
	implements AdminStoreOrderStatMonthlyQueryRepository {

	public AdminStoreOrderStatMonthlyQueryRepositoryImpl(JPAQueryFactory queryFactory) {
		super(
			queryFactory,
			QAdminStoreOrderStatMonthly.adminStoreOrderStatMonthly,
			QAdminStoreOrderStatMonthly.adminStoreOrderStatMonthly.date
		);
	}
}
