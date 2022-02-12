package com.codecafe.scheduling.writer;

import com.codecafe.scheduling.entity.TargetProduct;
import com.codecafe.scheduling.model.TargetProductWithAction;
import com.codecafe.scheduling.repository.TargetProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class TargetProductsWriter implements ItemWriter<TargetProductWithAction> {

    private final TargetProductsRepository targetProductsRepository;

    public TargetProductsWriter(TargetProductsRepository targetProductsRepository) {
        this.targetProductsRepository = targetProductsRepository;
    }

    @Override
    public void write(List<? extends TargetProductWithAction> items) {
        log.info("==> Entered inside TargetProductsWriter::write method");

        List<TargetProduct> targetProducts = new ArrayList<>();

        items.forEach(item -> {
            log.info("TargetProductsWriter::write handling - item [{}] id [{}]", item.getTargetProduct().getName(), item.getTargetProduct().getId());

            if (item.isDeleted())
                targetProductsRepository.deleteById(item.getTargetProduct().getId());
            else
                targetProducts.add(item.getTargetProduct());
        });

        if (!isEmpty(targetProducts))
            targetProductsRepository.saveAll(targetProducts);

        log.info("<== Exiting from TargetProductsWriter::write method");
    }

}
