package com.codecafe.scheduling.listener;

import com.codecafe.scheduling.entity.BatchJobExecutionDetail;
import com.codecafe.scheduling.repository.BatchJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JobExecutionListener extends JobExecutionListenerSupport {

    @Autowired
    private BatchJobRepository batchJobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("BEFORE JOB EXECUTION");

        jobExecution.getExecutionContext().put("lastCompletedJobTime", getLastCompletedJobTime());
    }

    private Optional<Date> getLastCompletedJobTime() {
        log.info("==> Entered inside JobExecutionListener::getLastCompletedJobTime method");

        Optional<Date> lastCompletedJobTime = Optional.empty();

        Optional<BatchJobExecutionDetail> lastCompletedJob = batchJobRepository.getLastSuccessfulJob("importProductsJob", "COMPLETED");

        if (lastCompletedJob.isPresent()) {
            log.info("######## Last Job [{}] status: [{}]", lastCompletedJob.get().getJobExecutionId(), lastCompletedJob.get().getStatus());
            lastCompletedJobTime = Optional.of(lastCompletedJob.get().getStartTime());
        }

        log.info("<== Exiting from JobExecutionListener::getLastCompletedJobTime method");
        return lastCompletedJobTime;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results!");
        } else {
            log.info("!!! JOB was not COMPLETED! Time to investigate!");
        }
    }

}