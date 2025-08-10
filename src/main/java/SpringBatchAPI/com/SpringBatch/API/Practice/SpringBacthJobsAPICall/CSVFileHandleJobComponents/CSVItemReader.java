package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.CSVFileHandleJobComponents;




import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Customer;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Utilities.DateUtils;

public class CSVItemReader extends FlatFileItemReader<Customer> {

    public CSVItemReader(String inputFile) {
        this.setResource(new FileSystemResource(inputFile));
        this.setLinesToSkip(1);
        this.setName("csvItemReader");

        // Configure tokenizer for CSV columns
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("index", "customerId", "firstName", "lastName", "company",
                           "city", "country", "phone1", "phone2", "email", "subscriptionDate", "website");

        // Map fields to Customer object
        FieldSetMapper<Customer> fieldSetMapper = new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                return Customer.builder()
                    .customerId(fieldSet.readString("customerId"))
                    .firstName(fieldSet.readString("firstName"))
                    .lastName(fieldSet.readString("lastName"))
                    .company(fieldSet.readString("company"))
                    .city(fieldSet.readString("city"))
                    .country(fieldSet.readString("country"))
                    .phone1(fieldSet.readString("phone1"))
                    .phone2(fieldSet.readString("phone2"))
                    .email(fieldSet.readString("email"))
                    .website(fieldSet.readString("website"))
                    .subscriptionDate(DateUtils.parseDate(fieldSet.readString("subscriptionDate")))
                    .build();
            }
        };

        // Setup line mapper combining tokenizer and fieldset mapper
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.afterPropertiesSet();

        this.setLineMapper(lineMapper);
    }
}
