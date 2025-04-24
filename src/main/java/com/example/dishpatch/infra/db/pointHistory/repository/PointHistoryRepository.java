package com.example.dishpatch.infra.db.pointHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, CustomPointHistoryRepository {

	@Query("SELECT SUM(p.remain) FROM PointHistory p WHERE p.user.id = :userId AND p.pointUsed = :pointUsed")
	Integer sumRemainByUserIdAndPointUsed(@Param("userId") Long userId, @Param("pointUsed") PointUsed pointUsed);
}
