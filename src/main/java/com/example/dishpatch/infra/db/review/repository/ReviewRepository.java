package com.example.dishpatch.infra.db.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dishpatch.infra.db.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findAllByStoreIdOrderByCreatedDate(Long storeId);

	@Query("""
		    SELECT r
		    FROM Review r
		    LEFT JOIN FETCH r.user
		    LEFT JOIN FETCH r.store
		    LEFT JOIN FETCH r.menu
		    WHERE r.rating BETWEEN :min AND :max
		""")
	List<Review> findReviewsByRatingBetween(@Param("min") int min, @Param("max") int max);

}
