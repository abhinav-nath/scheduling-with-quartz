package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.repository.SourceProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SourceProductsReader implements ItemReader<SourceProduct> {

    private int nextProductIndex;
    private List<SourceProduct> sourceProducts;

    private final SourceProductsRepository sourceProductsRepository;

    @Autowired
    private JobExplorer jobExplorer;


    public SourceProductsReader(SourceProductsRepository sourceProductsRepository) {
        this.sourceProductsRepository = sourceProductsRepository;
    }

    @Override
    public SourceProduct read() {
        log.info("==> Entered inside SourceProductsReader::read method");

        Optional<Date> lastCompletedJobTime = getLastCompletedJobTime();

        if (lastCompletedJobTime.isPresent())
            sourceProducts = sourceProductsRepository.findByLastSuccessfulJobRun(lastCompletedJobTime.get());
        else
            sourceProducts = sourceProductsRepository.findAll();

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

    private Optional<Date> getLastCompletedJobTime() {
        Optional<Date> lastCompletedJobTime = Optional.empty();
        List<JobInstance> jobInstances = jobExplorer.getJobInstances("importProductsJob", 0, 10);

        Optional<JobInstance> lastCompletedJobInstanceOptional = jobInstances.stream().filter(jobInstance -> {
            long instanceId = jobInstance.getInstanceId();
            return BatchStatus.COMPLETED == jobExplorer.getJobExecution(instanceId).getStatus();
        }).findFirst();

        if (lastCompletedJobInstanceOptional.isPresent()) {
            log.info("######## Last Job [{}] status: [{}]", lastCompletedJobInstanceOptional.get().getInstanceId(), jobExplorer.getJobExecution(lastCompletedJobInstanceOptional.get().getInstanceId()).getStatus());
            lastCompletedJobTime = Optional.of(jobExplorer.getLastJobExecution(jobExplorer.getJobInstance(lastCompletedJobInstanceOptional.get().getInstanceId())).getEndTime());
        }
        return lastCompletedJobTime;
    }

}