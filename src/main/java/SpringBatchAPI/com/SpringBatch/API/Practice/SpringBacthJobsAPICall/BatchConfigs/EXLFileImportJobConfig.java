package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.BatchConfigs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.EXLFileImportJobComponents.EXLFileJobListener;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.EXLFileImportJobComponents.EXLFileJobProcessor;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.EXLFileImportJobComponents.EXLFileReader;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Person;
import jakarta.persistence.EntityManagerFactory;

@EnableBatchProcessing
@Configuration
public class EXLFileImportJobConfig {
	
	
	// creating Job for accessing CSV file Handling
		@Bean
	    public Job EXLJob(@Qualifier(value = "EXLJobStep") Step EXLJobStep, JobRepository jobRepository, EXLFileJobListener listener) {
	        return new JobBuilder("EXLFileJob", jobRepository)
	                .incrementer(new RunIdIncrementer())
	                .validator(new JobParametersValidator() {
	                    @Override
	                    public void validate(JobParameters parameters) throws JobParametersInvalidException {
	                        if ("Costa Rica".equals(parameters.getString("ignoreCountry"))) {
	                            throw new JobParametersInvalidException("Invalid configuration");
	                        }
	                    }
	                })
	                .listener(listener)
	                .flow(EXLJobStep)
	                .end()
	                .build();
	    }
		
		
		
		
		
		
		// creating JobStep 
		@Bean
	    public Step EXLJobStep(@Qualifier(value = "EXLReader") ItemReader<Person> reader, @Qualifier(value = "EXLWriter") ItemWriter<Person> writer,
	                                JobRepository jobRepository, PlatformTransactionManager transactionManager,
	                                EXLFileJobProcessor processor) {
	        return new StepBuilder("csvStep", jobRepository)
	                .<Person, Person>chunk(50, transactionManager)
	                .reader(reader)
	                .processor(processor)
	                .writer(writer)
	                .allowStartIfComplete(true)
	                .build();
	    }
		
		
		
		
		
		// creating reader bean
		@Bean(name = "EXLReader")
		@StepScope
	    public ItemReader<Person> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
	        return new EXLFileReader(inputFile);
	    }
		
		
		@Bean(name = "EXLWriter")
	    public JpaItemWriter<Person> writer(EntityManagerFactory entityManagerFactory) {
	        return new JpaItemWriterBuilder<Person>()
	                .entityManagerFactory(entityManagerFactory)
	                .build();
	    }

}
