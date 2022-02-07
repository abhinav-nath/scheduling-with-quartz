package com.codecafe.scheduling.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class QuartzConfiguration {

    @Value("${scheduler.products.cron:*/10 * * * * ? *}")
    private String schedulerProductsCron;

    @Autowired
    private ApplicationContext applicationContext;

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

    /*@Bean(name = "jobRepository")
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
            log.error("JobRepository bean could not be initialized", ex);
        }
        return null;
    }*/

    /*@Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }*/

    /*@Bean
    public JobOperator jobOperator() {
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher());
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobRepository(jobRepository());
        return jobOperator;
    }*/

   /*@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }*/

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job, DataSource quartzDataSource, SpringBeanJobFactory jobFactory) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        log.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(jobFactory);
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        // Comment the following line to use the default Quartz job store.
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(jobDetailFactoryBean().getObject());
        factory.setCronExpression(schedulerProductsCron);
        factory.setName("cronTriggerFactoryBean");
        return factory;
    }

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(QuartzJobLauncher.class);
        factory.setDurability(true);
        return factory;
    }

}