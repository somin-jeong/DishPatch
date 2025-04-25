package com.example.dishpatch.infra.db.pointHistory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;
import com.example.dishpatch.infra.db.pointHistory.entity.PointUsed;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, CustomPointHistoryRepository {

	@Query("SELECT SUM(p.remain) FROM PointHistory p WHERE p.user.id = :userId AND p.pointUsed = :pointUsed")
	Integer sumRemainByUserIdAndPointUsed(@Param("userId") Long userId, @Param("pointUsed") PointUsed pointUsed);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE PointHistory p SET p.deletedDate = CURRENT_TIMESTAMP WHERE p.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
