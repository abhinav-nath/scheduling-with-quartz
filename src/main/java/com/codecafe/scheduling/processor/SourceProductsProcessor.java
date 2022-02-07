package com.codecafe.scheduling.processor;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.entity.TargetProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class SourceProductsProcessor implements ItemProcessor<List<SourceProduct>, List<TargetProduct>> {

    @Override
    public List<TargetProduct> process(List<SourceProduct> sourceProducts) {
      //  log.info("==> Entered inside SourceProductsProcessor::process method for source product [{}]", sourceProduct.getName());

        List<TargetProduct> targetProducts = new ArrayList<>();
        sourceProducts.forEach(sourceProduct ->
                targetProducts.add(TargetProduct.builder()
                        .id(sourceProduct.getId())
                        .name(sourceProduct.getName().toUpperCase(Locale.ROOT))
                        .createdAt(sourceProduct.getCreatedAt())
                        .modifiedAt(sourceProduct.getModifiedAt())
                        .build())
        );

       // log.info("<== Exiting from SourceProductsProcessor::process method with target product [{}]", targetProduct.getName());
        return targetProducts;
    }

}