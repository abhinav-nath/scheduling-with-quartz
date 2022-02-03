package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.repository.SourceProductsRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SourceProductsReader implements ItemReader<SourceProduct> {

    private int nextProductIndex;
    private List<SourceProduct> sourceProducts;

    private final SourceProductsRepository sourceProductsRepository;

    public SourceProductsReader(SourceProductsRepository sourceProductsRepository) {
        this.sourceProductsRepository = sourceProductsRepository;
    }

    @Override
    public SourceProduct read() throws Exception {
        sourceProducts = sourceProductsRepository.findAll();
        SourceProduct nextProduct = null;

        if (nextProductIndex < sourceProducts.size()) {
            nextProduct = sourceProducts.get(nextProductIndex);
            nextProductIndex++;
        } else {
            nextProductIndex = 0;
        }

        return nextProduct;
    }

}