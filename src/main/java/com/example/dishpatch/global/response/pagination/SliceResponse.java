package com.example.dishpatch.global.response.pagination;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SliceResponse<T> implements Serializable {
	List<T> content;
	boolean hasNext;
	Long nextCursorId;
	int size;
	int contentSize;
	boolean empty;

	public static <T extends CursorSupport> SliceResponse<T> from(Slice<T> slice) {
		List<T> content = slice.getContent();
		Long nextCursorId = content.isEmpty() ? null : content.get(content.size() - 1).getId();

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
