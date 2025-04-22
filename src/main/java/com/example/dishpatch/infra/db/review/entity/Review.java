package com.example.dishpatch.infra.db.review.entity;

import com.example.dishpatch.infra.db.common.BaseEnity;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.store.entity.Store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "review")
public class Review extends BaseEnity {

    private static final String DEFAULT_IMAGE_URL = "https://cdn-icons-png.flaticon.com/512/847/847969.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int rating;

    @Column(length = 500)
    private String contents;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl = DEFAULT_IMAGE_URL;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status; //ENUM ACTIVE,UNACTIVE
}
