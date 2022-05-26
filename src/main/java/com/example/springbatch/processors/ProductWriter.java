package com.example.springbatch.processors;

import com.example.springbatch.dto.ProductDto;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import javax.sql.DataSource;
import java.util.List;

public class ProductWriter extends JdbcBatchItemWriter<ProductDto> {

    public ProductWriter(DataSource dataSource) {
        setDataSource(dataSource);
        setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<>()
        );
        setSql("INSERT INTO PRODUCT (ID, NAME, DESCRIPTION, PRICE) VALUES (:id, :name, :description, :price)");
    }

    @Override
    public void write(List<? extends ProductDto> items) throws Exception {
        System.err.println(items);
        super.write(items);
    }
}
