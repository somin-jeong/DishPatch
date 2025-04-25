package com.example.dishpatch.domain.pointHistory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

	private final PointHistoryRepository pointHistoryRepository;
	private final UserRepository userRepository;
	private final PointUseHistoryService pointUseHistoryService;

	public Integer getRemainingPoint(Long userId) {

		return pointHistoryRepository.sumRemainByUserIdAndPointUsed(
			userId, PointUsed.UNUSED);
	}

	@Transactional
	public void usePoint(Long userId, Integer point) {
		List<PointUseHistory> pointUseHistoryList = pointHistoryRepository.applyUserPointUsage(userId, point);

		pointUseHistoryService.savePointUseHistory(pointUseHistoryList);
	}

	public void getPoint(Long userId, Integer totalPrice) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		PointHistory pointHistory = new PointHistory(totalPrice / 100, totalPrice / 100, user);

		pointHistoryRepository.save(pointHistory);

	}
}
