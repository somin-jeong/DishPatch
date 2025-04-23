package com.example.dishpatch.domain.pointHistory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

	private final PointHistoryRepository pointHistoryRepository;

	public Integer getRemainingPoint(Long userId) {

		return pointHistoryRepository.sumRemainByUserIdAndPointUsed(
			userId, PointUsed.UNUSED);
	}

	@Transactional
	public void usePoint(Long userId, Integer point) {

		List<PointHistory> pointHistories = pointHistoryRepository
			.findByUserIdAndStatusOrderByCreatedDateAsc(userId, PointUsed.UNUSED);

		int index = 0;

		while (point > 0) {
			int curPoint = pointHistories.get(index).getRemain();
			
			if (curPoint > point) {
				curPoint -= point;
				point = 0;
				pointHistories.get(index).updateRemain(curPoint);
			} else {
				point -= curPoint;
				pointHistories.get(index).updateRemain(0);
			}

			index++;
		}
	}
}
