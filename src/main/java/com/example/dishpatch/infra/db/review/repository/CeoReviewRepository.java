package com.example.dishpatch.infra.db.review.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.review.entity.CeoReview;

public interface CeoReviewRepository extends JpaRepository<CeoReview, Long> {
	@Modifying(clearAutomatically = true)
	@Query("""
		    UPDATE CeoReview cr
		    SET cr.deletedDate = :now
		    WHERE cr.review.id IN (
		        SELECT cr.id FROM Review r WHERE r.store.id = :storeId AND r.deletedDate IS NOT NULL
		    )
		""")
	int bulkSoftDeleteByStoreId(Long storeId, LocalDateTime now);
}
