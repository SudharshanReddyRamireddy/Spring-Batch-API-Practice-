package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "Person")
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    String customerId;
    String firstName;
    String lastName;
    String company;
    String city;
    String country;
    String phone1;
    String phone2;
    String email;
    LocalDate subscriptionDate;
    String website;
}
