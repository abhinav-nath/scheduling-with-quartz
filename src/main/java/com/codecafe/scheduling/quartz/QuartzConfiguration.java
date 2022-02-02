package com.codecafe.scheduling.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class QuartzConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzConfiguration.class);

    private final DataSource dataSource;
    private final JobExplorer jobExplorer;
    private final JobLocator jobLocator;
    private final JobRegistry jobRegistry;
    private final PlatformTransactionManager platformTransactionManager;

    @Value("${scheduler.cron}")
    private String schedulerCron;

    /*
    Quartz Scheduler Cron Format
    Format [ * * * * * ? * ]
    ------>[ 1 2 3 4 5 6 7 ]
    [1] : Seconds
    [2] : Minutes
    [3] : Hours
    [4] : Day of month
    [5] : Month
    [6] : Day of week
    [7] : Year
     */

    @Autowired
    public QuartzConfiguration(DataSource dataSource, JobExplorer jobExplorer, JobLocator jobLocator,
                               JobRegistry jobRegistry, PlatformTransactionManager platformTransactionManager) {
        this.dataSource = dataSource;
        this.jobExplorer = jobExplorer;
        this.jobLocator = jobLocator;
        this.jobRegistry = jobRegistry;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(platformTransactionManager);
        factoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factoryBean.setTablePrefix("BATCH_");
        try {
            factoryBean.afterPropertiesSet();
            return factoryBean.getObject();
        } catch (Exception ex) {
            LOG.error("JobRepository bean could not be initialized", ex);
        }
        return null;
    }

    @Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public JobOperator jobOperator() {
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher());
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobRepository(jobRepository());
        return jobOperator;
    }

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(QuartzJobLauncher.class);
        Map<String, Object> map = new HashMap<>();
        map.put("jobName", "importPersonJob");
        map.put("jobLauncher", jobLauncher());
        map.put("jobLocator", jobLocator);
        factory.setJobDataAsMap(map);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(jobDetailFactoryBean().getObject());
        stFactory.setCronExpression(schedulerCron);
        stFactory.setName("cronTriggerFactoryBean");
        return stFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(cronTriggerFactoryBean().getObject());
        return scheduler;
    }

}