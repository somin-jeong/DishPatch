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
		          AND (
		                (:min IS NOT NULL AND :max IS NOT NULL AND r.rating BETWEEN :min AND :max)
		                OR (:min IS NULL AND :max IS NOT NULL AND r.rating = :max)
		                OR (:min IS NOT NULL AND :max IS NULL AND r.rating = :min)
		          )
		""")
	List<Review> findAllByStoreIdAndRating(@Param("storeId") Long storeId, @Param("min") int min,
		@Param("max") int max);

}
