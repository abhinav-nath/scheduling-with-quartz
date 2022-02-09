package com.codecafe.scheduling.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JobExecutionListener extends JobExecutionListenerSupport {

    @Autowired
    private JobExplorer jobExplorer;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("BEFORE JOB EXECUTION");

        jobExecution.getExecutionContext().put("lastCompletedJobTime", getLastCompletedJobTime());
    }

    private Date getLastCompletedJobTime() {
        log.info("==> Entered inside JobExecutionListener::getLastCompletedJobTime method");
        Date lastCompletedJobTime = null;
        List<JobInstance> jobInstances = jobExplorer.getJobInstances("importProductsJob", 0, 1000);

        Optional<JobInstance> lastCompletedJobInstanceOptional = jobInstances.stream().filter(jobInstance -> {
            long instanceId = jobInstance.getInstanceId();
            return BatchStatus.COMPLETED == jobExplorer.getJobExecution(instanceId).getStatus();
        }).findFirst();

        if (lastCompletedJobInstanceOptional.isPresent()) {
            log.info("######## Last Job [{}] status: [{}]", lastCompletedJobInstanceOptional.get().getInstanceId(), jobExplorer.getJobExecution(lastCompletedJobInstanceOptional.get().getInstanceId()).getStatus());
            lastCompletedJobTime = jobExplorer.getLastJobExecution(jobExplorer.getJobInstance(lastCompletedJobInstanceOptional.get().getInstanceId())).getStartTime();
        }

        log.info("<== Exiting from JobExecutionListener::getLastCompletedJobTime method");

        return lastCompletedJobTime;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
        }
    }

}