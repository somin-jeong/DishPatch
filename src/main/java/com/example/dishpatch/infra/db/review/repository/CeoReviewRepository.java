package com.example.dishpatch.infra.db.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dishpatch.infra.db.review.entity.CeoReview;

public interface CeoReviewRepository extends JpaRepository<CeoReview, Long> {
}
