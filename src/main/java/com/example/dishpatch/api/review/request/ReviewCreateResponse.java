package com.example.dishpatch.api.review.request;

import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;

import lombok.Getter;

@Getter
public class ReviewCreateResponse {

	private Long id;
	private Long menuId;
	private int rating;
	private String contents;
	private String imageUrl;
	private ReviewStatus status;

	public ReviewCreateResponse(Review review) {
		this.id = review.getId();
		this.menuId = review.getMenu().getId(); //Menu Entity Getter() 없어서
		this.rating = review.getRating();
		this.contents = review.getContents();
		this.imageUrl = review.getImageUrl();
		this.status = review.getStatus();
	}
}
