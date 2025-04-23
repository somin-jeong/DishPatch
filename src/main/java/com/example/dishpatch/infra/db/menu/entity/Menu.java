package com.example.dishpatch.infra.db.menu.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.dishpatch.infra.db.common.SoftDeletableEntity;
import com.example.dishpatch.infra.db.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "menus")
public class Menu extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 40)
	private String name;

	@Column(nullable = false)
	private Integer price;

	private String imageUrl;

	@Column(nullable = false)
	private boolean soldOut;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@OneToMany(mappedBy = "menu")
	private List<MenuOption> options = new ArrayList<>();

	@Builder
	public Menu(String name, Integer price, String imageUrl, boolean soldOut, Store store) {
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
		this.soldOut = soldOut;
		this.store = store;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Menu menu) {
			return id != null && id.equals(menu.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
