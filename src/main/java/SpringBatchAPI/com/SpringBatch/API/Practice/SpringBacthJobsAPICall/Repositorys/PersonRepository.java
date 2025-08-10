package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Person;

public interface PersonRepository extends JpaRepository<Person, String>{

}
