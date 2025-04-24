package com.example.dishpatch.infra.db.admin.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class AdminStoreStat {

	@Id
	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private Integer orderCount;

	@Column(nullable = false)
	private Long totalSales;
}
