package com.example.dishpatch.infra.db.pointHistory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;
import com.example.dishpatch.infra.db.pointHistory.entity.QPointHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomPointHistoryRepositoryImpl implements CustomPointHistoryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<PointHistory> findUnusedPointsOrderByCreatedDateAsc(Long userId) {
		QPointHistory qPointHistory = QPointHistory.pointHistory;

		return queryFactory
			.selectFrom(qPointHistory)
			.where(
				qPointHistory.user.id.eq(userId),
				qPointHistory.pointUsed.eq(PointUsed.UNUSED)
			)
			.orderBy(qPointHistory.createdDate.asc())
			.fetch();
	}
}
