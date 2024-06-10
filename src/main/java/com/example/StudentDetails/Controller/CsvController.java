package com.example.StudentDetails.Controller;


import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.Helper.CsvHelper;
import com.example.StudentDetails.Model.ResponseMessage;
import com.example.StudentDetails.Service.CsvService;

@RestController
public class CsvController {

	@Autowired
	CsvService fileService;
  
	@GetMapping("/getstudentdetail/csv")
	public ResponseEntity<Resource> getFile() {
		String filename = "students.csv";
		InputStreamResource file = new InputStreamResource(fileService.load());

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			.contentType(MediaType.parseMediaType("application/csv"))
			.body(file);
	}
	
	@GetMapping("/getstudentdetail/csv/{student_id}")
	public ResponseEntity<Resource> getFile(@PathVariable int student_id) {
		String filename = "student.csv";
		InputStreamResource file = new InputStreamResource(fileService.loadById(student_id));

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			.contentType(MediaType.parseMediaType("application/csv"))
			.body(file);
	}
	
	@PostMapping(path = "/csv/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file) {
		String message = "";
		String error = "";
	
		if (CsvHelper.hasCSVFormat(file)) {
			try {
				
				System.out.println("Save is called");
				String result = fileService.save(file);
				
				if (result.equalsIgnoreCase("invalid")) {
					
					ByteArrayInputStream resultFile = CsvHelper.addErrorColumnToCSV(file.getInputStream());
					InputStreamResource errorFile = new InputStreamResource(resultFile);

					return ResponseEntity.badRequest()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "errors.csv")
						.contentType(MediaType.parseMediaType("application/csv"))
						.body(errorFile);
					
				}else {
				
					System.out.println("Returned from save");
					message = result + file.getOriginalFilename();
			        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
				}
			} catch (Exception e) {
				
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				error = e.getMessage();
		        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,error));
			}
	    }

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
}