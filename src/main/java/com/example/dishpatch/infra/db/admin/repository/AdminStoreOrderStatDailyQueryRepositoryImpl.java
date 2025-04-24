package com.example.dishpatch.infra.db.admin.repository;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.admin.entity.QAdminStoreOrderStatDaily;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class AdminStoreOrderStatDailyQueryRepositoryImpl
	extends AbstractAdminStoreOrderStatQueryRepository<AdminStoreOrderStatDaily, QAdminStoreOrderStatDaily>
	implements AdminStoreOrderStatDailyQueryRepository {

	public AdminStoreOrderStatDailyQueryRepositoryImpl(JPAQueryFactory queryFactory) {
		super(
			queryFactory,
			QAdminStoreOrderStatDaily.adminStoreOrderStatDaily,
			QAdminStoreOrderStatDaily.adminStoreOrderStatDaily.date
		);
	}
}
