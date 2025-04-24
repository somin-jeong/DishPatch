package com.example.dishpatch.infra.db.statistics.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;

import io.lettuce.core.dynamic.annotation.Param;

public interface StoreOrderStatDailyRepository
	extends JpaRepository<StoreOrderStatDaily, StoreOrderStatId> {

	@Query("""
			SELECT s
			FROM StoreOrderStatDaily s
			WHERE s.id.storeId = :storeId
			  AND s.id.date >= :from AND s.id.date < :to
		""")
	List<StoreOrderStatDaily> findByStoreIdAndDateRange(
		@Param("storeId") Long storeId,
		@Param("from") LocalDate from,
		@Param("to") LocalDate to
	);

}
