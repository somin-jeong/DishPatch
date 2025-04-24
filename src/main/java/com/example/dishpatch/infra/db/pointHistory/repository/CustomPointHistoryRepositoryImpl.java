package com.example.dishpatch.infra.db.pointHistory.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;
import com.example.dishpatch.infra.db.pointHistory.entity.QPointHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.transaction.Transactional;
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

	@Override
	@Transactional
	public List<PointUseHistory> applyUserPointUsage(Long userId, Integer point) {
		QPointHistory q = QPointHistory.pointHistory;

		List<PointHistory> pointHistories = queryFactory
			.selectFrom(q)
			.where(q.user.id.eq(userId)
				.and(q.pointUsed.eq(PointUsed.UNUSED)))
			.orderBy(q.createdDate.asc())
			.fetch();

		List<PointUseHistory> usedHistories = new ArrayList<>();

		for (PointHistory ph : pointHistories) {
			if (point == 0)
				break;

			int remain = ph.getRemain();
			if (remain > point) {
				ph.updateRemain(remain - point);
				usedHistories.add(new PointUseHistory(ph, point));
				point = 0;
			} else {
				point -= remain;
				usedHistories.add(new PointUseHistory(ph, remain));
				ph.updateRemain(0);
			}
		}
		return usedHistories;
	}

}
