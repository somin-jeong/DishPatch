package com.example.dishpatch.global.response.pagination;

import java.util.List;

import org.springframework.data.domain.Slice;

public record SliceResponse<T>(
	List<T> content,
	boolean hasNext,
	Long nextCursorId,
	int size,
	int contentSize,
	boolean empty
) {
	public static <T extends CursorSupport> SliceResponse<T> from(Slice<T> slice) {
		List<T> content = slice.getContent();
		Long nextCursorId = content.isEmpty() ? null : content.get(content.size() - 1).getCursorId();

		return new SliceResponse<>(
			content,
			slice.hasNext(),
			nextCursorId,
			slice.getSize(),
			content.size(),
			slice.isEmpty()
		);
	}
}
