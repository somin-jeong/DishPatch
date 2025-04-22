package com.example.dishpatch.infra.db.order.entity;

import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


}
