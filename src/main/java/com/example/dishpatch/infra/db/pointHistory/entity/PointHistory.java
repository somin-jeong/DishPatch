package com.example.dishpatch.infra.db.pointHistory.entity;

import com.example.dishpatch.infra.db.common.BaseEnity;
import com.example.dishpatch.infra.db.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PointHistory extends BaseEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int remain;

    @Enumerated(EnumType.STRING)
    private PointUsed pointUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
