package com.example.dishpatch.infra.batch.config;

import static com.example.dishpatch.infra.batch.config.FirstDayOfMonthDecider.*;

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
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class StoreOrderStatBatchConfig {

	@Bean
	public Job storeOrderStatJob(
		JobRepository jobRepository,
		Step storeOrderStatDailyStep,
		Step adminStoreOrderStatDailyStep,
		Step storeOrderStatMonthlyStep,
		Step adminStoreOrderStatMonthlyStep,
		FirstDayOfMonthDecider firstDayOfMonthDecider
	) {
		return new JobBuilder("storeOrderStatJob", jobRepository)
			.start(storeOrderStatDailyStep)
			.next(adminStoreOrderStatDailyStep)
			.next(firstDayOfMonthDecider)
				.on(FIRST_DAY)
					.to(storeOrderStatMonthlyStep).next(adminStoreOrderStatMonthlyStep)
					.from(firstDayOfMonthDecider)
				.on(NOT_FIRST_DAY).end()
			.end()
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
			.<Order, StoreOrderStatDaily>chunk(1000, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
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
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.backOffPolicy(exponentialBackOffPolicy())
			.skip(Exception.class)
			.skipLimit(1)
			.build();
	}

	@Bean
	public Step storeOrderStatMonthlyStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		@Qualifier("storeOrderStatMonthlyReader")
		ItemReader<StoreOrderStatDaily> reader,
		ItemProcessor<StoreOrderStatDaily, StoreOrderStatMonthly> processor,
		ItemWriter<StoreOrderStatMonthly> writer
	) {
		return new StepBuilder("storeOrderStatMonthlyStep", jobRepository)
			.<StoreOrderStatDaily, StoreOrderStatMonthly>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.backOffPolicy(exponentialBackOffPolicy())
			.skip(Exception.class)
			.skipLimit(1)
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
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.backOffPolicy(exponentialBackOffPolicy())
			.skip(Exception.class)
			.skipLimit(1)
			.build();
	}

	@Bean
	public ExponentialBackOffPolicy exponentialBackOffPolicy() {
		ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
		policy.setInitialInterval(1000L);
		policy.setMultiplier(2.0);
		policy.setMaxInterval(30000L);
		return policy;
	}
}
