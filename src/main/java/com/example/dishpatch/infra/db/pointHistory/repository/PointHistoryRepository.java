package com.example.dishpatch.infra.db.pointHistory.repository;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
