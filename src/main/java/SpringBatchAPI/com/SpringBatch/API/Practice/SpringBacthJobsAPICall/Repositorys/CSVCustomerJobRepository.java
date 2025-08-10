package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Customer;

public interface CSVCustomerJobRepository extends JpaRepository<Customer, String>{

}
