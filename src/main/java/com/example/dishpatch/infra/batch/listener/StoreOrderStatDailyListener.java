package com.example.dishpatch.infra.batch.listener;

import java.time.LocalDate;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StoreOrderStatDailyListener implements JobExecutionListener {

	private final JobLauncher jobLauncher;
	private final Job storeOrderStatMonthlyJob;
	private final Job adminStoreOrderStatDailyJob;
	private final Job adminStoreOrderStatMonthlyJob;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getJobInstance().getJobName().equals("storeOrderStatDailyJob")
			&& !jobExecution.getStatus().isUnsuccessful()
		) {
			try {
				jobLauncher.run(adminStoreOrderStatDailyJob,
					new JobParametersBuilder()
						.addLong("time", System.currentTimeMillis())
						.toJobParameters());
			} catch (Exception e) {
				log.error("Failed to launch admin daily job", e);
			}
			if (LocalDate.now().getDayOfMonth() == 1) {
				try {
					jobLauncher.run(storeOrderStatMonthlyJob,
						new JobParametersBuilder()
							.addLong("time", System.currentTimeMillis())
							.toJobParameters());
				} catch (Exception e) {
					log.error("Failed to launch monthly job", e);
				}
				try {
					jobLauncher.run(adminStoreOrderStatMonthlyJob,
						new JobParametersBuilder()
							.addLong("time", System.currentTimeMillis())
							.toJobParameters());
				} catch (Exception e) {
					log.error("Failed to launch admin monthly job", e);
				}
			}
		}
	}
}