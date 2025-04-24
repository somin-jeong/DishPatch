package com.example.dishpatch.infra.db.menu.entity;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_options")
public class MenuOption extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	private Integer price;

	@Column(nullable = false)
	private boolean soldOut;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Builder
	public MenuOption(String name, Integer price, boolean soldOut, Menu menu) {
		this.name = name;
		this.price = price;
		this.soldOut = soldOut;
		this.menu = menu;
	}

	public void update(String name, Integer price, boolean soldOut) {
		this.name = name;
		this.price = price;
		this.soldOut = soldOut;
	}
}
