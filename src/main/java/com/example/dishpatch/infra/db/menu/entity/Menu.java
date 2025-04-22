package com.example.dishpatch.infra.db.menu.entity;

import com.example.dishpatch.infra.db.store.entity.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 100)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isSoldOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu_id")
    private List<MenuOption> options = new ArrayList<>();

}
