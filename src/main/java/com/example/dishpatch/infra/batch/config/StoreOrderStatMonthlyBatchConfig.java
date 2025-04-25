package com.example.dishpatch.infra.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class StoreOrderStatMonthlyBatchConfig {

	@Bean
	public Job storeOrderStatMonthlyJob(JobRepository jobRepository, Step storeOrderStatMonthlyStep) {
		return new JobBuilder("storeOrderStatMonthlyJob", jobRepository)
			.start(storeOrderStatMonthlyStep)
			.build();
	}

	@Bean
	public Step storeOrderStatMonthlyStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<StoreOrderStatDaily> reader,
		ItemProcessor<StoreOrderStatDaily, StoreOrderStatMonthly> processor,
		ItemWriter<StoreOrderStatMonthly> writer
	) {
		return new StepBuilder("storeOrderStatMonthlyStep", jobRepository)
			.<StoreOrderStatDaily, StoreOrderStatMonthly>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
