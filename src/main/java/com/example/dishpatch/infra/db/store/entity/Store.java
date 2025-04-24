package com.example.dishpatch.infra.db.store.entity;

import java.time.LocalTime;

import com.example.dishpatch.api.store.request.StoreCreateRequest;
import com.example.dishpatch.api.store.request.StoreUpdateRequest;
import com.example.dishpatch.infra.db.common.SoftDeletableEntity;
import com.example.dishpatch.infra.db.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Store extends SoftDeletableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	private String image;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private int deliveryFee;

	private String introduction;

	@Column(nullable = false)
	private int minDeliveryPrice;

	private double rating;

	private int dibCount;

	private int reviewCount;

	@Column(nullable = false)
	private boolean isAdvertised;

	@Column(nullable = false)
	private LocalTime openTime;

	@Column(nullable = false)
	private LocalTime closeTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Store(String name, String address, String image, String phone, int deliveryFee, String introduction,
		int minDeliveryPrice, boolean isAdvertised, LocalTime openTime, LocalTime closeTime, Category category,
		User user) {
		this.name = name;
		this.address = address;
		this.image = image;
		this.phone = phone;
		this.deliveryFee = deliveryFee;
		this.introduction = introduction;
		this.minDeliveryPrice = minDeliveryPrice;
		this.isAdvertised = isAdvertised;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.category = category;
		this.user = user;
	}

	public static Store of(StoreCreateRequest request, User user, Category category) {
		return Store.builder()
			.name(request.name())
			.address(request.address())
			.phone(request.phone())
			.image(request.imageUrl())
			.introduction(request.introduction())
			.deliveryFee(request.deliveryFee())
			.minDeliveryPrice(request.minDeliveryPrice())
			.isAdvertised(request.isAdvertised())
			.openTime(request.getOpenTime())
			.closeTime(request.getCloseTime())
			.user(user)
			.category(category)
			.build();
	}

	public void update(StoreUpdateRequest request, Category category) {
		this.name = request.name();
		this.address = request.address();
		this.image = request.imageUrl();
		this.phone = request.phone();
		this.deliveryFee = request.deliveryFee();
		this.introduction = request.introduction();
		this.minDeliveryPrice = request.minDeliveryPrice();
		this.isAdvertised = request.isAdvertised();
		this.openTime = request.getOpenTime();
		this.closeTime = request.getCloseTime();
		this.category = category;
	}
}
