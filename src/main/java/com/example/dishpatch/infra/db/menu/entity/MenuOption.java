package com.example.dishpatch.infra.db.menu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_options")
public class MenuOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isSoldOut;

}
