package com.example.springbatch.processors;

import com.example.springbatch.dto.ProductDto;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<ProductDto, ProductDto> {
    @Override
    public ProductDto process(ProductDto item) throws Exception {
        return item;
    }
}
