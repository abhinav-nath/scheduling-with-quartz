package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import com.codecafe.scheduling.repository.SourceProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SourceProductsReader implements ItemReader<List<SourceProduct>> {

    private int nextProductIndex;
    private List<SourceProduct> sourceProducts;

    private final SourceProductsRepository sourceProductsRepository;

    public SourceProductsReader(SourceProductsRepository sourceProductsRepository) {
        this.sourceProductsRepository = sourceProductsRepository;
    }

    @Override
    public List<SourceProduct> read() {
        log.info("==> Entered inside SourceProductsReader::read method");

        sourceProducts = sourceProductsRepository.findAll();
        /*SourceProduct nextProduct = null;

        if (nextProductIndex < sourceProducts.size()) {
            nextProduct = sourceProducts.get(nextProductIndex);
            nextProductIndex++;
        } else {
            nextProductIndex = 0;
        }

        log.info("<== Exiting from SourceProductsReader::read method with source product [{}]", nextProduct != null ? nextProduct.getName() : null);
        */
        return sourceProducts;
    }

}