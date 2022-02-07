package com.codecafe.scheduling.quartz;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
public class QuartzJobLauncher extends QuartzJobBean {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.info("Job [{}] with id [{}] was completed successfully", job.getName(), jobExecution.getId());
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException ex) {
            log.error("Failed to execute job !!!");
            log.error(ex.getMessage());
        }
    }

}