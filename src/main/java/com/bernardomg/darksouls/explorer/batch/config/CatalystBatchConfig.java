
package com.bernardomg.darksouls.explorer.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.bernardomg.darksouls.explorer.batch.DBLogProcessor;
import com.bernardomg.darksouls.explorer.batch.model.CatalystBatchData;

@Configuration
@ConditionalOnProperty(prefix = "initialize.db.source", name = "catalyst")
public class CatalystBatchConfig {

    @Value("classPath:/data/catalysts.csv")
    private Resource           data;

    @Autowired
    private DataSource         datasource;

    @Autowired
    private JobBuilderFactory  jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public CatalystBatchConfig() {
        super();
    }

    @Bean("catalystItemReader")
    public ItemReader<CatalystBatchData> getCatalystItemReader(
            final LineMapper<CatalystBatchData> lineMapper) {
        return new FlatFileItemReaderBuilder<CatalystBatchData>()
            .name("catalystItemReader")
            .resource(data)
            .delimited()
            .names(new String[] { "name", "description", "weight", "durability",
                    "strength", "dexterity", "intelligence", "faith" })
            .linesToSkip(1)
            .lineMapper(lineMapper)
            .build();
    }

    @Bean("catalystItemWriter")
    public ItemWriter<CatalystBatchData> getCatalystItemWriter() {
        return new JdbcBatchItemWriterBuilder<CatalystBatchData>()
            .itemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<CatalystBatchData>())
            .sql(
                "INSERT INTO catalysts (name, description, weight, durability, strength, dexterity, intelligence, faith) VALUES (:name, :description, :weight, :durability, :strength, :dexterity, :intelligence, :faith)")
            .dataSource(datasource)
            .build();
    }

    @Bean("catalystLineMapper")
    public LineMapper<CatalystBatchData> getCatalystLineMapper() {
        final DelimitedLineTokenizer lineTokenizer;
        final BeanWrapperFieldSetMapper<CatalystBatchData> fieldSetMapper;
        final DefaultLineMapper<CatalystBatchData> lineMapper;

        lineMapper = new DefaultLineMapper<CatalystBatchData>();

        lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(
            new String[] { "name", "description", "weight", "durability",
                    "strength", "dexterity", "intelligence", "faith" });
        lineTokenizer.setIncludedFields(new int[] { 0, 2, 3, 4, 6, 7, 8, 9 });
        fieldSetMapper = new BeanWrapperFieldSetMapper<CatalystBatchData>();
        fieldSetMapper.setTargetType(CatalystBatchData.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean("catalystLoaderJob")
    public Job getCatalystLoaderJob(
            @Qualifier("catalystLoaderStep") final Step armorLoaderStep) {
        return jobBuilderFactory.get("catalystLoaderJob")
            .incrementer(new RunIdIncrementer())
            .start(armorLoaderStep)
            .build();
    }

    @Bean("catalystLoaderStep")
    public Step getCatalystLoaderStep(
            final ItemReader<CatalystBatchData> reader,
            final ItemWriter<CatalystBatchData> writer) {
        return stepBuilderFactory.get("catalystLoaderStep")
            .<CatalystBatchData, CatalystBatchData> chunk(5)
            .reader(reader)
            .processor(new DBLogProcessor<>())
            .writer(writer)
            .build();
    }

}