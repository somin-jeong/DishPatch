package com.example.dishpatch.infra.db.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private CouponType coupontype;

    @Column(nullable = false)
    private String deductedPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponUsed status;
}
