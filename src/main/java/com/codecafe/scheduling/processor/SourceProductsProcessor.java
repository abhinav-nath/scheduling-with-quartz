package com.codecafe.scheduling.processor;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.entity.TargetProduct;
import com.codecafe.scheduling.model.TargetProductWithAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class SourceProductsProcessor implements ItemProcessor<SourceProduct, TargetProductWithAction> {

    @Override
    public TargetProductWithAction process(SourceProduct sourceProduct) {
        log.info("==> Entered inside SourceProductsProcessor::process method - source product [{}] id [{}]", sourceProduct.getName(), sourceProduct.getId());

        TargetProductWithAction targetProductWithAction = new TargetProductWithAction();
        TargetProduct targetProduct = TargetProduct.builder()
                .id(sourceProduct.getId())
                .name(sourceProduct.getName().toUpperCase(Locale.ROOT))
                .createdAt(sourceProduct.getCreatedAt())
                .modifiedAt(sourceProduct.getModifiedAt())
                .build();
        targetProductWithAction.setTargetProduct(targetProduct);

        if (sourceProduct.isDeleted())
            targetProductWithAction.setDeleted(true);

        log.info("<== Exiting from SourceProductsProcessor::process method - target product [{}] id [{}]", targetProduct.getName(), targetProduct.getId());
        return targetProductWithAction;
    }

}