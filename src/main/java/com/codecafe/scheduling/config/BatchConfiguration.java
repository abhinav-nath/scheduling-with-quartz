package com.codecafe.scheduling.config;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.entity.TargetProduct;
import com.codecafe.scheduling.listener.JobCompletionNotificationListener;
import com.codecafe.scheduling.processor.SourceProductsProcessor;
import com.codecafe.scheduling.reader.SourceProductsReader;
import com.codecafe.scheduling.writer.TargetProductsWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public Job importProductsJob(JobCompletionNotificationListener listener, Step ingestProductsStep) {
        return jobBuilderFactory.get("importProductsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(ingestProductsStep)
                .end()
                .build();
    }

    @Bean
    public Step ingestProductsStep(SourceProductsReader reader, SourceProductsProcessor processor, TargetProductsWriter writer) {
        return stepBuilderFactory.get("ingestProductsStep")
                .<SourceProduct, TargetProduct>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}