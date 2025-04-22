package com.example.dishpatch.infra.db.store.entity;

import com.example.dishpatch.infra.db.common.BaseEnity;
import com.example.dishpatch.infra.db.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Store extends BaseEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String image;

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
    private boolean advertisement;

    @Enumerated(EnumType.STRING)
    private StoreStatus status; // ACTIVE, INACTIVE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;
}
