package com.example.dishpatch.infra.db.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@EntityGraph(attributePaths = "ceoReview")
	@Query("""
		    SELECT r FROM Review r
		          WHERE r.store.id = :storeId
		          AND r.rating BETWEEN :min AND :max
				  AND  (r.status = 'PUBLIC' or r.user.id = :userId)
				  ORDER BY r.createdDate DESC
		""")
	Page<Review> findAllByStoreIdAndRating(
		@Param("userId") Long userId,
		@Param("storeId") Long storeId,
		@Param("min") Integer min,
		@Param("max") Integer max,
		Pageable pageable);

	@Modifying(clearAutomatically = true)
	@Query("""
		    UPDATE Review r SET r.deletedDate = CURRENT_TIMESTAMP WHERE r.store.id = :storeId
		""")
	void bulkSoftDeleteByStoreId(Long storeId);

	@Modifying(clearAutomatically = true)
	@Query(value = """
				UPDATE review r 
				JOIN store s ON r.store_id = s.id
				SET r.deleted_date = CURRENT_TIMESTAMP
				WHERE s.user_id = :userId
		""", nativeQuery = true)
	void deleteAllByUserId(Long userId);

	@Modifying(clearAutomatically = true)
	void deleteById(Long reviewId);

	@EntityGraph(attributePaths = {"user"})
	Optional<Review> findById(Long reviewId);
}
