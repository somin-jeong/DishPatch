package com.example.dishpatch.infra.db.store.entity;

import java.time.LocalTime;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Store extends SoftDeletableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private int deliveryFee;

	@Column(nullable = false)
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
	@JoinColumn(name = "owner_id", nullable = false)
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
}
