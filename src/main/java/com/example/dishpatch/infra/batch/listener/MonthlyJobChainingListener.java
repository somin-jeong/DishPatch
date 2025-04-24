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
public class MonthlyJobChainingListener implements JobExecutionListener {

	private final JobLauncher jobLauncher;
	private final Job storeOrderStatMonthlyJob;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getJobInstance().getJobName().equals("storeOrderStatDailyJob")
			&& !jobExecution.getStatus().isUnsuccessful()
			&& LocalDate.now().getDayOfMonth() == 1
		) {
			try {
				log.info("Daily job completed. Launching monthly job.");
				jobLauncher.run(storeOrderStatMonthlyJob,
					new JobParametersBuilder()
						.addLong("time", System.currentTimeMillis())
						.toJobParameters());
			} catch (Exception e) {
				log.error("Failed to launch monthly job after daily", e);
			}
		}
	}
}