package com.example.dishpatch.domain.pointHistory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;
import com.example.dishpatch.infra.db.pointHistory.entity.QPointHistory;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

	private final PointHistoryRepository pointHistoryRepository;
	private final UserRepository userRepository;

	@Autowired
	private JPAQueryFactory queryFactory;

	public Integer getRemainingPoint(Long userId) {

		return pointHistoryRepository.sumRemainByUserIdAndPointUsed(
			userId, PointUsed.UNUSED);
	}

	@Transactional
	public void usePoint(Long userId, Integer point) {

		QPointHistory qPointHistory = QPointHistory.pointHistory;

		List<PointHistory> pointHistories = queryFactory
			.selectFrom(qPointHistory)
			.where(qPointHistory.user.id.eq(userId)
				.and(qPointHistory.pointUsed.eq(PointUsed.UNUSED)))
			.orderBy(qPointHistory.createdDate.asc())
			.fetch();

		for (PointHistory pointHistory : pointHistories) {
			if (point == 0)
				break;

			int curPoint = pointHistory.getRemain();

			if (curPoint > point) {
				curPoint -= point;
				point = 0;
				pointHistory.updateRemain(curPoint);
			} else {
				point -= curPoint;
				pointHistory.updateRemain(0);
			}
		}
	}

	public void getPoint(Long userId, Integer totalPrice) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		PointHistory pointHistory = new PointHistory(totalPrice / 100, totalPrice / 100, user);

		pointHistoryRepository.save(pointHistory);

	}
}
