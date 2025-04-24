package com.example.dishpatch.infra.db.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("""
		    SELECT r FROM Review r
		          WHERE r.store.id = :storeId
		          AND r.rating BETWEEN :min AND :max
				  AND (
		            		  r.user.id = :userId
		            		  OR r.status = 'PUBLIC'
		          		  )
				  ORDER BY r.createdDate DESC
		""")
	List<Review> findAllByStoreIdAndRating(
		@Param("userId") Long userId,
		@Param("storeId") Long storeId,
		@Param("min") Integer min,
		@Param("max") Integer max);

	/**
	 * 가게 폐업 시 리뷰 삭제
	 * @param storeId
	 */
	void deleteAllByStoreId(Long storeId);

}
