package com.example.StudentDetails.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Model.ErrorResponse;
import com.example.StudentDetails.Model.StudentFilter;
import com.example.StudentDetails.Service.StudentDetailsService;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class StudentDetailsController {
	
	Logger logger = LoggerFactory.getLogger(StudentDetailsController.class);
	
	@Autowired 
	private StudentDetailsService students;
		
	@GetMapping("/getAllStudentDetails")
	public ResponseEntity<?> getAllStudents() {
		try {
			
			logger.info("Action: All student details is requested.");
			List<Student> allStudents = students.findAllStudents();
			return ResponseEntity.status(HttpStatus.OK).body(allStudents);
		
		}catch(Exception e) {
			
			logger.error("Failed to retrieve data" + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to retrieve data: "+ e.getMessage());
		}	
	}
	
	@GetMapping("/getStudent/{student_id}")
	public ResponseEntity<?> getStudentById(@PathVariable String student_id) {
	
		int id = Integer.parseInt(student_id);
		logger.info("Action: Student Details for " + "student_id : " + student_id + " is requested.");
		return students.findStudentById(id);
		
	}
	
	@DeleteMapping("/deleteStudent/{student_id}")
	public ResponseEntity<?> deleteStudentById(@PathVariable int student_id) {
		
		logger.info("Action: Student with "+"student_id : " + student_id + " is requested to delete");
		return students.deleteStudentById(student_id);
	}
	
	@PostMapping("/addStudent")
	public ResponseEntity<?> addStudent(@RequestBody Student studentDetails) {
		try {
		
			logger.info("Action: Student with student_name : " + studentDetails.getName() + " is requested to add the details to database.");
			ResponseEntity<?> result = students.addStudent(studentDetails);
			
			if (((String) result.getBody().toString()).equalsIgnoreCase("Student Added Successfully")) {
				
				return getAllStudents();
			}else {
				return result;
			}
			
		
		}catch(Exception e) {
			
			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "Unable to add Student", e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/updateStudent/{student_id}")
	public ResponseEntity<?> updateStudent(@RequestBody Student studentDetails, @PathVariable int student_id) {
		
		logger.info("Action: Student with student_id : " + student_id + " is requested to update details");
		return students.updateStudentDetails(studentDetails,student_id);
	}
	
	@GetMapping("/allStudents/sort/{order}/{field}")
	public List<Student> findStudentDetailsWithSorting(@PathVariable String field, @PathVariable String order){
		
		logger.info("Action: findStudentDetailsWithSorting() method is called to to get students With sorting");
		return students.findStudentDetailsWithSorting(order,field);
		
	}
	
	@GetMapping("/allStudents/{pageNo}/{pageSize}")
	public Page<Student> findStudentDetailsWithPageing(@PathVariable int pageNo, @PathVariable int pageSize){
		
		logger.info("Action: findStudentDetailsWithPageing() method is called to to get students With Paging");
		return students.findStudentDetailsWithPaging(pageNo,pageSize);
		
	}
	
	@GetMapping("/allStudents/{pageNo}/{pageSize}/search/{value}")
	public List<Student> findStudentDetailsBySearching(@PathVariable String value,@PathVariable int pageNo,@PathVariable int pageSize){
		
		logger.info("Action: findStudentDetailsBySearching() method is called to to get students by searching");
		return students.findStudentDetailsBySearching(value,pageNo,pageSize);
		
	}
	
	@GetMapping("/allStudents/filter/{pageNo}/{pageSize}")
	public Page<Student> findStudentDetailsWithFilter(@RequestBody StudentFilter filter, @PathVariable int pageNo, @PathVariable int pageSize){
		
		logger.info("Action: findStudentDetailsWithFilter() method is called to to get students With Filters");
		return students.findStudentDetailsWithFilter(filter,pageNo, pageSize);
		
	}

	@ControllerAdvice
	class GlobalControllerExceptionHandler {
		
		@ExceptionHandler(HttpMessageNotReadableException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
			
			logger.error("Invalid request body input: " + ex.getMessage());
	        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid request body input" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
		
		@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	    @ResponseStatus(HttpStatus.BAD_GATEWAY)
	    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
			
			logger.error("Invalid request Method type: " + ex.getMessage());
	        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_GATEWAY,"Invalid request Method type" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }

	    @ExceptionHandler(NumberFormatException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException ex) {
	    	
	    	logger.error("Invalid input parameter format: " + ex.getMessage());
	    	ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid input parameter format" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    @ExceptionHandler(PropertyReferenceException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
			
			logger.error("Invalid propert: " + ex.getMessage());
	        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid property" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }

	    @ExceptionHandler(MissingPathVariableException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<ErrorResponse> handleMissingPathVariableException(MissingPathVariableException ex) {
	    	
	    	logger.error("No Path Variable found : " + ex.getMessage());
	    	ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"No Path Variable found." , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    @ExceptionHandler(NullPointerException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
	    	
	    	logger.error("No results found : " + ex.getMessage());
	    	ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"No results found" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    @ExceptionHandler(EmptyResultDataAccessException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
	    	
	    	logger.error("Empty data error: " + ex.getMessage());
	    	ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND,"No Student found with provided Student_id" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    @ExceptionHandler(SQLSyntaxErrorException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<ErrorResponse> handleSQLSyntaxErrorException(EmptyResultDataAccessException ex) {
	    	
	    	logger.error("Incorrect SQL query : " + ex.getMessage());
	    	ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND,"Incorrect SQL query" , ex.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

}