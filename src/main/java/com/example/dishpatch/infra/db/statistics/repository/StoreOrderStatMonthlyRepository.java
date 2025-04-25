package com.example.dishpatch.infra.db.statistics.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

import io.lettuce.core.dynamic.annotation.Param;

public interface StoreOrderStatMonthlyRepository
	extends JpaRepository<StoreOrderStatMonthly, StoreOrderStatId> {

	@Query("""
			SELECT s
			FROM StoreOrderStatMonthly s
			WHERE s.id.storeId = :storeId
			  AND s.id.date >= :from AND s.id.date < :to
		""")
	List<StoreOrderStatMonthly> findByStoreIdAndDateRange(
		@Param("storeId") Long storeId,
		@Param("from") LocalDate from,
		@Param("to") LocalDate to
	);

	@Query("""
			SELECT s
			FROM StoreOrderStatMonthly s
			WHERE s.id.date = :date
		""")
	List<StoreOrderStatMonthly> findAllByDate(@Param("date") LocalDate date);

}
