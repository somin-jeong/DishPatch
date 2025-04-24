package com.example.dishpatch.infra.db.pointHistory.repository;

import java.util.List;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;

public interface CustomPointHistoryRepository {
	List<PointHistory> findUnusedPointsOrderByCreatedDateAsc(Long userId);
}
