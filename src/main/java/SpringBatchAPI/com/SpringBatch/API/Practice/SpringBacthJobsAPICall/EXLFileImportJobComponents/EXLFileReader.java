package SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.EXLFileImportJobComponents;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;

import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Models.Person;
import SpringBatchAPI.com.SpringBatch.API.Practice.SpringBacthJobsAPICall.Utilities.DateUtils;

@StepScope
public class EXLFileReader implements ItemReader<Person>{

	 // This will be used to go row-by-row through the Excel sheet
    private Iterator<Row> rowIterator;

    // Number of header rows to skip (can change if needed)
    private static final int HEADER_ROWS_TO_SKIP = 1;

    public EXLFileReader(String inputFile) {
        try {
        	
            // Load the Excel file from the classpath (src/main/resources)
            InputStream inputStream = new FileInputStream(inputFile);

            // Open the Excel file as a workbook (.xlsx format)
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get the first sheet in the Excel workbook
            Sheet sheet = workbook.getSheetAt(0);

            // Initialize the row iterator
            rowIterator = sheet.iterator();

            // Skip header row(s)
            for (int i = 0; i < HEADER_ROWS_TO_SKIP && rowIterator.hasNext(); i++) {
                rowIterator.next(); // Move the iterator forward
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Excel reader", e);
        }
    }

    
    
    
    @Override
    public Person read() {
        // If there are more rows to read, process one row at a time
        if (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Debugging: Print all relevant cells from column 1 to 11
            for (int i = 1; i <= 11; i++) {
                System.out.println("Cell[" + i + "]: " + row.getCell(i));
            }

            // Build and return a Customer object using data from the row
            return Person.builder()
                    .customerId(getStringCellValue(row.getCell(1)))
                    .firstName(getStringCellValue(row.getCell(2)))
                    .lastName(getStringCellValue(row.getCell(3)))
                    .company(getStringCellValue(row.getCell(4)))
                    .city(getStringCellValue(row.getCell(5)))
                    .country(getStringCellValue(row.getCell(6)))
                    .phone1(getStringCellValue(row.getCell(7)))
                    .phone2(getStringCellValue(row.getCell(8)))
                    .email(getStringCellValue(row.getCell(9)))
                    .subscriptionDate(DateUtils.parseDate(getStringCellValue(row.getCell(10))))
                    .website(getStringCellValue(row.getCell(11)))
                    .build();
        }

        // No more rows — tell Spring Batch we are done reading
        return null;
    }
    
    
    

    // Safely read values from each Excel cell as a string
    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // You can format this if needed
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // To avoid decimals
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}
