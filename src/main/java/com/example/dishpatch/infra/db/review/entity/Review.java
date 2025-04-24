package com.example.dishpatch.infra.db.review.entity;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "review")
public class Review extends SoftDeletableEntity {

	private static final String DEFAULT_IMAGE_URL = "https://cdn-icons-png.flaticon.com/512/847/847969.png";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Column(nullable = false)
	private int rating;

	@Column(length = 500)
	private String contents;

	@Column(name = "image_url", columnDefinition = "LONGTEXT")
	private String imageUrl = DEFAULT_IMAGE_URL;

	@Enumerated(EnumType.STRING)
	private ReviewStatus status;

	public Review(User user, Store store, Menu menu, int rating, String contents, String imageUrl,
		ReviewStatus status) {
		this.user = user;
		this.store = store;
		this.menu = menu;
		this.rating = rating;
		this.contents = contents;
		this.imageUrl = imageUrl;
		this.status = status;
	}

	public void update(int rating, String contents, String imageUrl, ReviewStatus status) {
		this.rating = rating;
		this.contents = contents;
		this.imageUrl = imageUrl;
		this.status = status;
	}
}
