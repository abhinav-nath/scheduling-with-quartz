package com.codecafe.scheduling.processor;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.entity.TargetProduct;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SourceProductsProcessor implements ItemProcessor<SourceProduct, TargetProduct> {

    @Override
    public TargetProduct process(SourceProduct sourceProduct) throws Exception {
        System.out.println(">>>>> processor");
        return TargetProduct.builder()
                .id(sourceProduct.getId())
                .name(sourceProduct.getName().toUpperCase(Locale.ROOT))
                .createdAt(sourceProduct.getCreatedAt())
                .modifiedAt(sourceProduct.getModifiedAt())
                .build();
    }

}