package com.example.dishpatch.infra.db.coupon.entity;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;
import com.example.dishpatch.infra.db.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor
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

	@ManyToOne
	@JoinColumn(name = "user_Id",nullable = true)
	private User user;


	public void useCoupon() {
		this.status = CouponUsed.A;
	}

	@Builder
	public Coupon (String name, CouponType coupontype, Integer deductedPrice,
		Integer maxDiscount, CouponUsed status, User user){
		this.name = name;
		this.coupontype = coupontype;
		this.deductedPrice = deductedPrice;
		this.maxDiscount = maxDiscount;
		this.status = status;
		this.user = user;
	}
}
