package com.example.dishpatch.infra.db.menu.repository;

import java.util.List;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public interface MenuQueryRepository {

	List<Menu> findAllByStoreId(Long storeId);
}
