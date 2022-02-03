package com.codecafe.scheduling.writer;

import com.codecafe.scheduling.entity.TargetProduct;
import com.codecafe.scheduling.repository.TargetProductsRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargetProductsWriter implements ItemWriter<TargetProduct> {

    private final TargetProductsRepository targetProductsRepository;

    public TargetProductsWriter(TargetProductsRepository targetProductsRepository) {
        this.targetProductsRepository = targetProductsRepository;
    }

    @Override
    public void write(List<? extends TargetProduct> items) throws Exception {
        targetProductsRepository.saveAll(items);
    }

}
