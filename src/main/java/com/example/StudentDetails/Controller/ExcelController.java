package com.example.StudentDetails.Controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.example.StudentDetails.Helper.ExcelHelper;
import com.example.StudentDetails.Model.ResponseMessage;
import com.example.StudentDetails.Service.ExcelService;

@RestController
public class ExcelController {
	
	@Autowired
	ExcelService fileService;
	
	@GetMapping("/getstudentdetail/excel")
	public ResponseEntity<Resource> getFile() {
			 
		String filename = "students.xlsx";
		InputStreamResource file = new InputStreamResource(fileService.load(0));
		
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			.body(file);			 
	}
	
	@GetMapping("/getstudentdetail/excel/{student_id}")
	public ResponseEntity<Resource> getFile(@PathVariable int student_id){
		
		String filename = "student.xlsx";
		InputStreamResource file = new InputStreamResource(fileService.load(student_id));
		
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			.body(file);	
	}
	
	@PostMapping(path = "/excel/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file) {
		
		String message = "";

	    if (ExcelHelper.hasExcelFormat(file)) {
	    	try {
	    
	    		String result = fileService.save(file);
	    		
	    		if(result.equalsIgnoreCase("invalid")) {
	    			
	    			ByteArrayInputStream responseFile = ExcelHelper.addErrorColumnToExcel(file.getInputStream());
	    			InputStreamResource errorFile = new InputStreamResource(responseFile);

		    		return ResponseEntity.badRequest()
		    				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "error.xlsx")
		    				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
		    				.body(errorFile);
	    			
	    		}else {
	    			
	    			message = "Uploaded the file successfully: " + file.getOriginalFilename();
		    		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));	      
	    		}
	    		
	    	} catch (Exception e) {
	    		
	    		System.out.println(e.getMessage());
	    		message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	    		String error = e.getMessage();
	    		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,error));
	    	}
	    }

	    message = "Please upload an excel file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}  
	
	@GetMapping("/getclassdetail/excel")
	public ResponseEntity<Resource> getClassFile() {
			 
		String filename = "classes.xlsx";
		InputStreamResource file = new InputStreamResource(fileService.loadClassData());
		
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
			.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
			.body(file);			 
	}
}