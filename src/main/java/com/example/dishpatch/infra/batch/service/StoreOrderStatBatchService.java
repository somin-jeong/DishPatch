package com.example.dishpatch.infra.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatDailyRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreOrderStatBatchService {

	private final StoreOrderStatDailyRepository dailyRepository;
	private final EntityManager em;

	@Transactional
	public void saveDaily(List<StoreOrderStatDaily> stats) {
		if (stats.isEmpty()) {
			return;
		}

		List<StoreOrderStatId> ids = stats.stream()
			.map(StoreOrderStatDaily::getId)
			.toList();

		Map<StoreOrderStatId, StoreOrderStatDaily> existingMap = dailyRepository.findAllById(ids).stream()
			.collect(Collectors.toMap(StoreOrderStatDaily::getId, e -> e));

		List<StoreOrderStatDaily> toInsert = new ArrayList<>();
		List<StoreOrderStatDaily> toUpdate = new ArrayList<>();

		for (StoreOrderStatDaily stat : stats) {
			if (existingMap.containsKey(stat.getId())) {
				StoreOrderStatDaily existing = existingMap.get(stat.getId());
				if (!Objects.equals(existing, stat)) {
					existing.update(stat.getOrderCount(), stat.getTotalSales());
					toUpdate.add(existing);
				}
			} else {
				toInsert.add(stat);
			}
		}

		if (!toInsert.isEmpty()) {
			dailyRepository.saveAll(toInsert);
		}

		List<StoreOrderStatDaily> failedUpdateStats = new ArrayList<>();
		for (StoreOrderStatDaily stat : toUpdate) {
			try {
				dailyRepository.bulkUpdate(stat.getId(), stat.getOrderCount(), stat.getTotalSales());
			} catch (Exception e) {
				failedUpdateStats.add(stat);
			}
		}

		dailyRepository.flush();
		em.clear();

		if (!failedUpdateStats.isEmpty()) {
			retryDailyFailedUpdates(failedUpdateStats);
		}
	}

	private void retryDailyFailedUpdates(List<StoreOrderStatDaily> failedStats) {
		for (StoreOrderStatDaily stat : failedStats) {
			try {
				dailyRepository.bulkUpdate(stat.getId(), stat.getOrderCount(), stat.getTotalSales());
			} catch (Exception e) {
				log.error("Final failure after retry: stat = {}", stat.getId(), e);
			}
		}

		dailyRepository.flush();
		em.clear();
	}

}
