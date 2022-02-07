package com.codecafe.scheduling.writer;

import com.codecafe.scheduling.entity.TargetProduct;
import com.codecafe.scheduling.repository.TargetProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TargetProductsWriter implements ItemWriter<List<TargetProduct>> {

    private final TargetProductsRepository targetProductsRepository;

    public TargetProductsWriter(TargetProductsRepository targetProductsRepository) {
        this.targetProductsRepository = targetProductsRepository;
    }

    @Override
    public void write(List<? extends List<TargetProduct>> items) throws Exception {
        log.info("==> Entered inside TargetProductsWriter::write method");
        targetProductsRepository.saveAll((List<TargetProduct>) items);
        log.info("<== Exiting from TargetProductsWriter::write method");
    }

}
