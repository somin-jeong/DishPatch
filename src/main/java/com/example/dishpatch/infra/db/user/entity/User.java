package com.example.dishpatch.infra.db.user.entity;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user")
public class User extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
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
	private UserGrade grade; // 이넘 D 고마운분, C 귀한분, B 더귀한분, A 천생연분

	@Column(columnDefinition = "INT DEFAULT 0", nullable = false)
	private Integer subscribe; // ACTIVE(1),UNACTIVE(0)< 디폴트값

	@Enumerated(EnumType.STRING)
	private UserRole role; // 이넘 USER,CEO,ADMIN

	@Column(nullable = false)
	private String currentAddress;

}
