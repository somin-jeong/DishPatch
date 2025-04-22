package com.example.dishpatch.infra.db.review.repository;

import com.example.dishpatch.infra.db.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
