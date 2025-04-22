package com.example.dishpatch.infra.db.review.repository;

import com.example.dishpatch.infra.db.review.entity.CeoReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CeoReviewRepository extends JpaRepository<CeoReview, Long> {
}
