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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.CSVFileHandleJobComponents.CSVCustomerJobProcessor;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.CSVFileHandleJobComponents.CSVItemReader;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.CSVFileHandleJobComponents.CSVJobListener;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Customer;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Utilities.DateUtils;
import jakarta.persistence.EntityManagerFactory;


@Configuration
@EnableBatchProcessing
public class CSVJobConfigs {
	
	
	
	// creating Job for accessing CSV file Handling
	@Bean
    public Job CSVJob(@Qualifier(value = "CSVJobStep") Step CSVJobStep, JobRepository jobRepository, CSVJobListener listener) {
        return new JobBuilder("csvJob", jobRepository)
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
                .flow(CSVJobStep)
                .end()
                .build();
    }
	
	
	
	// creating JobStep 
	@Bean
    public Step CSVJobStep(CSVItemReader reader, ItemWriter<Customer> writer,
                                JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                CSVCustomerJobProcessor processor) {
        return new StepBuilder("csvStep", jobRepository)
                .<Customer, Customer>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }
	
	
	
	
	
	// Creating writer bean
	@Bean
    public JpaItemWriter<Customer> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
	
	
	
	
	
	// reader 
//	@Bean
//	@StepScope
//	public FlatFileItemReader<Customer> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
//	    return new FlatFileItemReaderBuilder<Customer>()
//	            .linesToSkip(1)
//	            .name("csvItemReader")
//	            .resource(new FileSystemResource(inputFile))  // Use FileSystemResource for external files
//	            .delimited()
//	            .delimiter(",")
//	            .names("index", "customerId", "firstName", "lastName", "company",
//	                    "city", "country", "phone1",
//	                    "phone2", "email", "subscriptionDate", "website")
//	            .fieldSetMapper(fieldSet -> Customer.builder()
//	                    .customerId(fieldSet.readString("customerId"))
//	                    .firstName(fieldSet.readString("firstName"))
//	                    .lastName(fieldSet.readString("lastName"))
//	                    .company(fieldSet.readString("company"))
//	                    .city(fieldSet.readString("city"))
//	                    .country(fieldSet.readString("country"))
//	                    .phone1(fieldSet.readString("phone1"))
//	                    .phone2(fieldSet.readString("phone2"))
//	                    .email(fieldSet.readString("email"))
//	                    .website(fieldSet.readString("website"))
//	                    .subscriptionDate(DateUtils.parseDate(fieldSet.readString("subscriptionDate")))
//	                    .build())
//	            .build();
//	}
	
	
	
	@Bean
	@StepScope
	public CSVItemReader reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
	    return new CSVItemReader(inputFile);
	}



}
