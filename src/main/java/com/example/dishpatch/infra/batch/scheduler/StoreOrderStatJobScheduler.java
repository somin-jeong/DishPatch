package com.example.dishpatch.infra.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StoreOrderStatJobScheduler {

	private final JobLauncher jobLauncher;
	private final Job storeOrderStatDailyJob;

	@Scheduled(cron = "0 0 16 * * *")
	public void run() {
		try {
			log.info("storeOrderStatDailyJob started");
			jobLauncher.run(storeOrderStatDailyJob,
				new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.toJobParameters());
		} catch (Exception e) {
			log.error("storeOrderStatDailyJob failed", e);
		}
	}
}
