package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.controllers;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/Jobs")
public class SpringBatchJobsAPIController {
	
	@Autowired
	private JobLauncher jobLauncher;  // note the spelling

	@Autowired
	@Qualifier("CSVJob")
	private Job CSVJob;
	
	
	@Autowired
	@Qualifier("EXLJob")
	private Job EXLJob;
	

    @Operation(summary = "Upload CSV file and start batch job")
    @PostMapping(value = "/CSV_file_Job", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> CSVFileJob(
        @Parameter(description = "CSV file to upload", required = true)
        @RequestParam("file") MultipartFile file) throws Exception {

        String tempDir = System.getProperty("java.io.tmpdir");
        File convFile = new File(tempDir + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        System.out.println("Uploaded file saved to: " + convFile.getAbsolutePath());
        
        
        
        // calling batch job
        jobLauncher.run(CSVJob, new JobParametersBuilder()
        		.addString("inputFile", convFile.getAbsolutePath())  // pass uploaded file path here
        	    .addString("ignoreCountry", "India")
        	    .addLong("time", System.currentTimeMillis()) // Unique each run
        	    .toJobParameters());

        System.out.println(".................JOB SUCCESSFULLY COMPLETED.................");
        return ResponseEntity.status(HttpStatus.OK).body("CSV FILE IMPORT JOB SUCCESS.");
    }
    
    
 //*************************************** EXL FILE JOB HANDLING *******************************
    
    
    // API For Handling Exlce file
    @Operation(summary = "Upload EXL file and start batch job")
    @PostMapping(value = "/EXL_file_Job", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> EXLFileJob(
        @Parameter(description = "EXL file to upload", required = true)
        @RequestParam("file") MultipartFile file) throws Exception {

        String tempDir = System.getProperty("java.io.tmpdir");
        File convFile = new File(tempDir + "/" + file.getOriginalFilename());
        file.transferTo(convFile);

        System.out.println("Uploaded file saved to: " + convFile.getAbsolutePath());
        
        
        
         //calling batch job
        jobLauncher.run(EXLJob, new JobParametersBuilder()
        		.addString("inputFile", convFile.getAbsolutePath())  // pass uploaded file path here
        	    .addString("ignoreCountry", "India")
        	    .addLong("time", System.currentTimeMillis()) // Unique each run
        	    .toJobParameters());

        System.out.println(".................JOB SUCCESSFULLY COMPLETED.................");
        return ResponseEntity.status(HttpStatus.OK).body("EXL FILE IMPORT JOB SUCCESS.");
    }
    
}
