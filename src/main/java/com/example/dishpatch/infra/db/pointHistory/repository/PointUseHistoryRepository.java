package com.example.dishpatch.infra.db.pointHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;

public interface PointUseHistoryRepository extends JpaRepository<PointUseHistory, Long> {
}
