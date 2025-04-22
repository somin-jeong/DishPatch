package com.example.dishpatch.infra.db.order.entity;

import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_id")
    private MenuOption menuOption;
}
