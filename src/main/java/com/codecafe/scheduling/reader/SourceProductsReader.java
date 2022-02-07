package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.repository.SourceProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SourceProductsReader implements ItemReader<SourceProduct> {

    private int nextProductIndex;
    private JobExecution jobExecution;
    private List<SourceProduct> sourceProducts;

    private final SourceProductsRepository sourceProductsRepository;

    @Autowired
    private JobExplorer jobExplorer;


    public SourceProductsReader(SourceProductsRepository sourceProductsRepository) {
        this.sourceProductsRepository = sourceProductsRepository;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SourceProduct read() {
        log.info("==> Entered inside SourceProductsReader::read method");

        Date lastSuccessfulJobRun = new Date();
        long instanceId = jobExplorer.getLastJobInstance("importProductsJob").getInstanceId();

        System.out.println("######## Last Job Status : " + jobExplorer.getLastJobExecution(jobExplorer.getJobInstance(instanceId)).getStatus());

        if (BatchStatus.COMPLETED == jobExplorer.getLastJobExecution(jobExplorer.getJobInstance(instanceId)).getStatus()) {
            System.out.println("*********************** YOOOOO *************************");
            lastSuccessfulJobRun = jobExplorer.getLastJobExecution(jobExplorer.getJobInstance(instanceId)).getEndTime();
        }

        log.info("Last successful job run was at [{}]", lastSuccessfulJobRun);

        sourceProducts = sourceProductsRepository.findByLastSuccessfulJobRun(lastSuccessfulJobRun);
        SourceProduct nextProduct = null;

        if (nextProductIndex < sourceProducts.size()) {
            nextProduct = sourceProducts.get(nextProductIndex);
            nextProductIndex++;
        } else {
            nextProductIndex = 0;
        }

        log.info("<== Exiting from SourceProductsReader::read method with source product [{}]", nextProduct != null ? nextProduct.getName() : null);
        return nextProduct;
    }

}