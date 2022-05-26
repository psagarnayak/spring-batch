package com.example.springbatch;

import com.example.springbatch.dto.ProductDto;
import com.example.springbatch.processors.ProductProcessor;
import com.example.springbatch.processors.ProductWriter;
import com.example.springbatch.processors.ProductsCsvReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobLauncher jobLauncher;
    private DataSource dataSource;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                       JobLauncher jobLauncher, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters params = new JobParametersBuilder().addLong("exectTime", System.currentTimeMillis()).toJobParameters();
        this.jobLauncher.run(createJob(), params);
    }

    @Bean
    public Job createJob(){
        return jobBuilderFactory.get("consume-csv").incrementer(new RunIdIncrementer()).start(createStep()).build();
    }

    @Bean
    public Step createStep() {
        return stepBuilderFactory.get("consume-csv")
                .<ProductDto, ProductDto>chunk(50)
                .reader(reader()).processor(processor()).writer(writer())
                .build();
    }

    @Bean
    public ItemReader<ProductDto> reader() {
        return new ProductsCsvReader();
    }

    @Bean
    public ItemProcessor<ProductDto, ProductDto> processor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<ProductDto> writer() {
        return new ProductWriter(dataSource);
    }
}
