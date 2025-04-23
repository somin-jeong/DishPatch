package com.example.dishpatch.infra.db.coupon.entity;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "coupons")
public class Coupon extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private CouponType coupontype;

	@Column(nullable = false)
	private Integer deductedPrice;

	@Column
	private Integer maxDiscount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponUsed status;
}
