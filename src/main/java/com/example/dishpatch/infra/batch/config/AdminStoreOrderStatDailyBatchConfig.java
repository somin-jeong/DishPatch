package com.example.dishpatch.infra.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class AdminStoreOrderStatDailyBatchConfig {

	@Bean
	public Job adminStoreOrderStatDailyJob(JobRepository jobRepository, Step adminStoreOrderStatDailyStep) {
		return new JobBuilder("adminStoreOrderStatDailyJob", jobRepository)
			.start(adminStoreOrderStatDailyStep)
			.build();
	}

	@Bean
	public Step adminStoreOrderStatDailyStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		@Qualifier("adminStoreOrderStatDailyReader")
		ItemReader<StoreOrderStatDaily> reader,
		ItemProcessor<StoreOrderStatDaily, AdminStoreOrderStatDaily> processor,
		ItemWriter<AdminStoreOrderStatDaily> writer
	) {
		return new StepBuilder("adminStoreOrderStatDailyStep", jobRepository)
			.<StoreOrderStatDaily, AdminStoreOrderStatDaily>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
