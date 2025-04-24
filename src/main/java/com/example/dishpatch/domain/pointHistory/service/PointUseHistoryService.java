package com.example.dishpatch.domain.pointHistory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.infra.db.pointHistory.entity.PointUseHistory;
import com.example.dishpatch.infra.db.pointHistory.repository.PointUseHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointUseHistoryService {

	private final PointUseHistoryRepository pointUseHistoryRepository;

	public void savePointUseHistory(List<PointUseHistory> pointUseHistoryList) {
		pointUseHistoryRepository.saveAll(pointUseHistoryList);
	}

}
