package com.example.dishpatch.infra.db.pointHistory.entity;

import com.example.dishpatch.infra.db.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "pointUseHistories")
public class PointUseHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer pointUsed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "point_history_id", nullable = false)
	private PointHistory pointHistory;

	public PointUseHistory(PointHistory ph, Integer point) {
		this.pointHistory = ph;
		this.pointUsed = point;
	}
}
