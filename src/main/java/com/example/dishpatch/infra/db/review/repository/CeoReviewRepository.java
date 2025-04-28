package com.example.dishpatch.infra.db.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.dishpatch.infra.db.review.entity.CeoReview;

public interface CeoReviewRepository extends JpaRepository<CeoReview, Long> {
	@Modifying(clearAutomatically = true)
	@Query("""
		    UPDATE CeoReview cr SET cr.deletedDate = CURRENT_TIMESTAMP
		    WHERE cr.review.id IN (
		        SELECT cr.id FROM Review r WHERE r.store.id = :storeId AND r.deletedDate IS NOT NULL
		    )
		""")
	int bulkSoftDeleteByStoreId(Long storeId);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE CeoReview c SET c.deletedDate = CURRENT_TIMESTAMP WHERE c.user.id = :userId AND c.deletedDate IS NULL")
	void deleteAllByUserId(Long userId);

	@EntityGraph(attributePaths = {"user"})
	Optional<CeoReview> findById(Long reviewId);

}
