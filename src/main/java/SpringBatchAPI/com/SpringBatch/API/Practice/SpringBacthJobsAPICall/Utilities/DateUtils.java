package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Utilities;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class DateUtils {

    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return LocalDate.now() ;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString.trim(), formatter);
    }
}



//@UtilityClass
//public class DateUtils {
//
//    public static LocalDate parseDate(String dateStr){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return LocalDate.parse(dateStr, formatter);
//    }
//
//}
