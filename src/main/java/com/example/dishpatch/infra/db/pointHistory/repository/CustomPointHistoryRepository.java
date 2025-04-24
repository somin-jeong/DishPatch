package com.example.dishpatch.infra.db.pointHistory.repository;

import java.util.List;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;

public interface CustomPointHistoryRepository {
	List<PointHistory> findUnusedPointsOrderByCreatedDateAsc(Long userId);

	public List<PointUseHistory> applyUserPointUsage(Long userId, Integer point);
}
