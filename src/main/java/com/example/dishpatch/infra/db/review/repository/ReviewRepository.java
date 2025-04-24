package com.example.dishpatch.infra.db.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

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

	/**
	 * 가게 폐업 시 리뷰 삭제
	 * @param storeId
	 */
	void deleteAllByStoreId(Long storeId);

}
