package com.example.dishpatch.infra.db.review.entity;

import com.example.dishpatch.infra.db.common.BaseEnity;
import com.example.dishpatch.infra.db.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "ceo_review")
public class CeoReview extends BaseEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500)
    private String contents;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status; //ENUM ACTIVE,UNACTIVE
}
