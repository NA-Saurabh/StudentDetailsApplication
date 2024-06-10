package com.example.StudentDetails.Helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.Model.Student;

public class CsvHelper {
	
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "studentId", "name", "registerNo", "gender", "age", "emailId", "phoneNo", "currentStatus", "course", "batch", "fees", "classId" };

	public static ByteArrayInputStream studentsToCSV(List<Student> students) {
		
		final CSVFormat format = CSVFormat.DEFAULT.withHeader(HEADERs).withQuoteMode(QuoteMode.MINIMAL);

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
	    	for (Student student : students) {
	    		
	    		List<String> data = Arrays.asList(
	    			String.valueOf(student.getStudentId()),
	    			student.getName(),
	    			String.valueOf(student.getRegisterNo()),
	    			student.getGender(),
	    			String.valueOf(student.getAge()),
	    			student.getEmailId(),
	    			student.getPhoneNo(),
	    			student.getCurrentStatus(),
	    			student.getCourse(),
	    			student.getBatch(),
	    			String.valueOf(student.getFees()),
	    			String.valueOf(student.getClassId())
	    	    );

	            csvPrinter.printRecord(data);
	        }

	    	csvPrinter.flush();
	    	return new ByteArrayInputStream(out.toByteArray());
	    
	    }catch (IOException e) {
	    	
	    	throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
	    }
	}
	
	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
	    }

	    return true;
	}
	
	public static List<Student> csvToStudents(InputStream is) {

	    try (
	        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
	        BufferedReader fileReader = new BufferedReader(isr)
	    ) {

	        if (fileReader.markSupported()) {
	            fileReader.mark(1);
	            int readChar = fileReader.read();
	            if (readChar != 0xFEFF) {
	                fileReader.reset();
	            }
	        }

	        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

	        List<Student> students = new ArrayList<>();

	        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

	        for (CSVRecord csvRecord : csvRecords) {
	            Student student = new Student(
	                0,
	                csvRecord.get("name").trim(),
	                Integer.parseInt(csvRecord.get("registerNo").trim()),
	                csvRecord.get("gender").trim(),
	                Integer.parseInt(csvRecord.get("age").trim()),
	                csvRecord.get("emailId").trim(),
	                csvRecord.get("phoneNo").trim(),
	                csvRecord.get("currentStatus").trim(),
	                csvRecord.get("course").trim(),
	                csvRecord.get("batch").trim(),
	                Integer.parseInt(csvRecord.get("fees").trim()),
	                Integer.parseInt(csvRecord.get("classId").trim())
	            );

	            students.add(student);
	        }

	        return students;

	    } catch (IOException e) {
	        throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    }
	}
	
	public static ByteArrayInputStream addErrorColumnToCSV(InputStream is) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			
			String line;
			int rowNumber = 0;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(","); 

				if (rowNumber == 0) {
					
					line += ",Error";
	            } else {
	                    
	            	String errorMessage = validateRow(fields);
	            	line += "," + errorMessage;
	            }

				writer.write(line);
				writer.newLine();
				rowNumber++;
			}
			writer.flush();

			return new ByteArrayInputStream(out.toByteArray());

	    } catch (IOException e) {
	            
	    	throw new RuntimeException("Failed to add error column to CSV file: " + e.getMessage());
	    }
	}

    private static String validateRow(String[] fields) {
        StringBuilder errorMessage = new StringBuilder();

        String gender = fields.length > 3 ? fields[3] : "";
        String phoneNo = fields.length > 6 ? fields[6] : "";
        
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
            errorMessage.append("Invalid gender. ");
        }

        
        if (phoneNo.length() != 10) {
            errorMessage.append("Invalid phone number length. ");
        }

        return errorMessage.toString().trim();
    }
}
