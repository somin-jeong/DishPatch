package com.example.dishpatch.api.statistics.response;

import java.util.List;

public record StoreOrderStatResponse(
	List<StoreOrderStatItem> stats
) {

}
