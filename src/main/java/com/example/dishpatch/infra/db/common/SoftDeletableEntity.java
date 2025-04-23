package com.example.dishpatch.infra.db.common;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

	private LocalDateTime deletedDate;

	public void softDelete() {
		this.deletedDate = LocalDateTime.now();
	}

	public boolean isDeleted() {
		return deletedDate != null;
	}
}
