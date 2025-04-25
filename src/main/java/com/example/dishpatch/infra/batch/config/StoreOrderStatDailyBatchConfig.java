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

import com.example.dishpatch.infra.batch.listener.StoreOrderStatDailyListener;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class StoreOrderStatDailyBatchConfig {

	private final StoreOrderStatDailyListener monthlyJobChainingListener;

	@Bean
	public Job storeOrderStatDailyJob(JobRepository jobRepository, Step storeOrderStatDailyStep) {
		return new JobBuilder("storeOrderStatDailyJob", jobRepository)
			.start(storeOrderStatDailyStep)
			.listener(monthlyJobChainingListener)
			.build();
	}

	@Bean
	public Step storeOrderStatDailyStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<Order> reader,
		ItemProcessor<Order, StoreOrderStatDaily> processor,
		ItemWriter<StoreOrderStatDaily> writer
	) {
		return new StepBuilder("storeOrderStatDailyStep", jobRepository)
			.<Order, StoreOrderStatDaily>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
}
