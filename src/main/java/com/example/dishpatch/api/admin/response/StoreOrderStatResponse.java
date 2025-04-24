package com.example.dishpatch.api.admin.response;

import java.util.List;

public record StoreOrderStatResponse(
	List<StoreOrderStatItem> stats
) {

}
