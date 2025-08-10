package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.EXLFileImportJobComponents;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@StepScope
public class EXLFileJobProcessor implements ItemProcessor<Person, Person>{
	
	
	private final String ignoreCountry;

    public EXLFileJobProcessor(@Value("#{jobParameters['ignoreCountry']}") String ignoreCountry) {
        this.ignoreCountry = ignoreCountry;
    }

    @Override
    public Person process(Person item) throws Exception {
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
