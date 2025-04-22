package com.example.dishpatch.infra.db.user.entity;

import com.example.dishpatch.infra.db.common.BaseEnity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user")
public class User extends BaseEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserProvider provider; //이넘 LOCAL,NAVER,KAKAO

    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserGrade Grade; // 이넘 D 고마운분, C 귀한분, B 더귀한분, A 천생연분

    @Column(columnDefinition = "INT DEFAULT 0",nullable = false)
    private Integer subscribe; // ACTIVE(1),UNACTIVE(0)< 디폴트값

    @Enumerated(EnumType.STRING)
    private UserRole role; // 이넘 USER,CEO,ADMIN

    @Column(nullable = false)
    private String currentAddress;

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 이넘 ACTIVE,UNACTIVE


}
