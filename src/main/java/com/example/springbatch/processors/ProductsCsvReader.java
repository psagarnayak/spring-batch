package com.example.springbatch.processors;

import com.example.springbatch.dto.ProductDto;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

public class ProductsCsvReader extends FlatFileItemReader<ProductDto> {

    public ProductsCsvReader() {
        setResource(new ClassPathResource("products.csv"));
        DefaultLineMapper<ProductDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id","name","description","price");
        tokenizer.setDelimiter(",");
        BeanWrapperFieldSetMapper<ProductDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductDto.class);
        fieldSetMapper.setDistanceLimit(2);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        setLineMapper(lineMapper);
    }

    @Override
    public ProductDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.err.println("Called Read");
        return super.read();
    }
}
