package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.CSVFileHandleJobComponents;



import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Customer;

@Slf4j
@JobScope
@Component
public class CSVCustomerJobProcessor implements ItemProcessor<Customer, Customer> {

    private final String ignoreCountry;

    public CSVCustomerJobProcessor(@Value("#{jobParameters['ignoreCountry']}") String ignoreCountry) {
        this.ignoreCountry = ignoreCountry;
    }

    @Override
    public Customer process(Customer item) throws Exception {
    	log.info("CUSTOMER : {}", item.toString());
        if (item.getFirstName().isEmpty() || item.getFirstName() == null) {
            log.info("Customer Ignored Due To Empty Name Field : {}", item.toString());
        }
        
        if(item.getCountry().equalsIgnoreCase(ignoreCountry) || item.getCountry().isEmpty() || item.getCountry() == null) {
        	log.info("CUstomer Ignored Due to country Field Issue : {}", item.toString());
        }
        
        return item;
    }
}
