package com.example.dishpatch.infra.batch.config;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class FirstDayOfMonthDecider implements JobExecutionDecider {

	public static final String FIRST_DAY = "FIRST_DAY";
	public static final String NOT_FIRST_DAY = "NOT_FIRST_DAY";

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		return LocalDate.now().getDayOfMonth() == 1
			? new FlowExecutionStatus(FIRST_DAY)
			: new FlowExecutionStatus(NOT_FIRST_DAY);
	}
}