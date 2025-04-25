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

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class AdminStoreOrderStatMonthlyBatchConfig {

	@Bean
	public Job adminStoreOrderStatMonthlyJob(JobRepository jobRepository, Step adminStoreOrderStatMonthlyStep) {
		return new JobBuilder("adminStoreOrderStatMonthlyJob", jobRepository)
			.start(adminStoreOrderStatMonthlyStep)
			.build();
	}

	@Bean
	public Step adminStoreOrderStatMonthlyStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<StoreOrderStatMonthly> reader,
		ItemProcessor<StoreOrderStatMonthly, AdminStoreOrderStatMonthly> processor,
		ItemWriter<AdminStoreOrderStatMonthly> writer
	) {
		return new StepBuilder("adminStoreOrderStatMonthlyStep", jobRepository)
			.<StoreOrderStatMonthly, AdminStoreOrderStatMonthly>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
