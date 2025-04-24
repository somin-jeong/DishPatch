package com.example.dishpatch.infra.db.pointHistory.entity;

import com.example.dishpatch.infra.db.common.BaseEntity;
import com.example.dishpatch.infra.db.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PointHistory extends BaseEntity {
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

	public PointHistory(Integer amount, Integer remain, User user) {
		this.amount = amount;
		this.remain = remain;
		this.user = user;
		this.pointUsed = PointUsed.UNUSED;
	}

	public void updateRemain(int curPoint) {
		if (curPoint == 0) {
			this.pointUsed = PointUsed.USED;
		}
		this.remain = curPoint;
	}
}
