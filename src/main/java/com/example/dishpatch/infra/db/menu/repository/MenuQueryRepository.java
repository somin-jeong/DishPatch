package com.example.dishpatch.infra.db.menu.repository;

import java.util.List;
import java.util.Optional;

import com.example.dishpatch.infra.db.menu.entity.Menu;

public interface MenuQueryRepository {

	List<Menu> findAllByStoreIdWithOptions(Long storeId);

	Optional<Menu> findByMenuId(Long menuId);
}
