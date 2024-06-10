package com.example.StudentDetails.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import com.example.StudentDetails.DAO.StudentDetailsDAO;
import com.example.StudentDetails.Helper.FilteredStudents;
import com.example.StudentDetails.Model.ErrorResponse;
import com.example.StudentDetails.Model.StudentFilter;
import com.example.StudentDetails.Repository.StudentDetailsRepository;
import com.example.StudentDetails.Model.Student;

@Service
public class StudentDetailsService {
	
	Logger logger = LoggerFactory.getLogger(StudentDetailsService.class);
	
	@Autowired
	private StudentDetailsDAO allStudents; 
	
	@Autowired
	StudentDetailsRepository studentRepository;

	
	public List<Student> findAllStudents() {
		
		logger.info("Action: StudentDetailsDAO.findAllStudents() method is called.");
		return allStudents.findAllStudents();
	}
	
	public ResponseEntity<?> findStudentById( int student_id){
		Student matchedStudent = null;
		
		try {
			
			logger.info("Action: StudentDetailsDAO.findStudentById() method is called for " + "student_id : " + student_id);
			matchedStudent=allStudents.findStudentById(student_id);				
		}catch(EmptyResultDataAccessException e) {
			
			logger.error("No Student Found for " + "student_id : " + student_id);
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,"No student found", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		logger.info("Student details with student_id " + student_id + " is retrieved.");
		return new ResponseEntity<Student>(matchedStudent,HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteStudentById(int student_id){
		
		logger.info("Action: StudentDetailsDAO.deleteStudentById() method is called to delete " + "student_id "+ student_id );
		int result = allStudents.deleteStudentById(student_id);
		
		if (result == 0) {
			
			logger.error("No Student Found with student_id: "+ student_id);
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,"No student found with provided student ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}else {
			
			logger.info("Student Details for " + "student_id: "+ student_id + " deleted successfully.");
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK,"Student Deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		}
	}
	
	public ResponseEntity<?> addStudent( Student studentDetails){
		studentDetails.setStudentId(0);
		
		try {
			logger.info("User Inputs \n Name: "+studentDetails.getName()+" Register no: "+studentDetails.getRegisterNo()+" Gender :"+studentDetails.getGender()+" Age "+studentDetails.getAge()+" Phone number "+studentDetails.getPhoneNo()+
					" Current Status: "+studentDetails.getCurrentStatus()+" Email Id: "+ studentDetails.getEmailId()+" Batch: "+ studentDetails.getBatch()+" Course: "+ studentDetails.getCourse()+" Fees: "+studentDetails.getFees());
				
		    if (validate(studentDetails, true).equalsIgnoreCase("valid")) {
		    	
		    	logger.info("Action: StudentDetailsDAO.addStudent() method is called to add student name: "+ studentDetails.getName());
				int result = allStudents.addStudent(studentDetails);
							
				if(result == 0) {
								
					logger.error("Student Details not added for student name: "+ studentDetails.getName());
					return new ResponseEntity<>("Student Details not added",HttpStatus.NOT_FOUND);
				}else{
								
					logger.info("Student Details for student name: " + studentDetails.getName() + " added successfully.");
					return new ResponseEntity<>("Student Added Successfully",HttpStatus.OK);
				}
		    }else {
		    	
		    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,validate(studentDetails, true),"Validation Failed");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

		    }
			
				
		}catch(BadSqlGrammarException ex) {
			
			logger.error("Bad SQL grammar: " + ex.getMessage());
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,"Bad SQL grammar", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			
		}catch(DataAccessException dae) {
			
			logger.error("Unable to connect DataBase : " + dae.getMessage());
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,"Unable to connect DataBase", dae.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			
		}catch(Exception e) {
			
			logger.error("Error: " + e.getMessage());
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,"Unable to add Student", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);	
		}
	}
	
	public ResponseEntity<?> updateStudentDetails(Student studentDetails,int student_id){
		
		studentDetails.setStudentId(student_id);
		
		if(validate(studentDetails, false).equalsIgnoreCase("valid")) {
			
			logger.info("Action: StudentDetailsDAO.updateStudentDetails() method is called to update student with student_id: " + student_id);
			int result = allStudents.updateStudentDetails(student_id,studentDetails);
			
			if(result == 0 && allStudents.checkForStudent(student_id) == 0) {
				
				logger.error("No student found with student_id: "+ student_id);
				return new ResponseEntity<>("Invalid Student Id",HttpStatus.NOT_FOUND);
			
			}else if(result == 0 && allStudents.checkForStudent(student_id) >= 1){
				
				logger.error("Student Details Already Updated for student with student_id: "+ student_id);
				ErrorResponse errorResponse = new ErrorResponse(HttpStatus.ALREADY_REPORTED,"Student Details Already updated");
	            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(errorResponse);
				
			}else{
				
				logger.info("Student Details updated successfully for student with student_id: "+ student_id);
				ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK,"Student details updated successfully");
	            return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
			}
			
		}else {
			
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,validate(studentDetails, false),"Validation Failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}	
	}
	
    public List<Student> findStudentDetailsWithSorting(String order, String field){
    	
    	if (order.equalsIgnoreCase("desc")) {
    		
    		logger.info("Student Details returned with sorting in descending order successfully.");
    		List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.DESC,field));
    		return students;
    	}else {
    		
    		logger.info("Student Details returned with sorting in ascending order successfully.");
    		List<Student> students = studentRepository.findAll(Sort.by(Sort.Direction.ASC,field));
    		return students;
    	}	
 	}
    
    public Page<Student> findStudentDetailsWithPaging(int pageNo,int pageSize ){
    	
    	logger.info("Student Details returned with paging successfully.");
		Page<Student> students = studentRepository.findAll(PageRequest.of(pageNo, pageSize));
		return students;
		
 	}
    
    public List<Student> findStudentDetailsBySearching( String value, int pageNo, int pageSize){
    	
    	if(studentRepository.findByNameContains(value, PageRequest.of(0,1)).size() > 0) {

    		logger.info("Searched Keyword matched on name field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByNameContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(isNumeric(value) && studentRepository.findByRegisterNoEquals(Integer.parseInt(value), PageRequest.of(0,1)).size() > 0){
    		
    		logger.info("Searched Keyword matched on register_no field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByRegisterNoEquals(Integer.parseInt(value), PageRequest.of(pageNo,pageSize));
    		
    	}else if(studentRepository.findByEmailIdContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on email_id field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByEmailIdContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(studentRepository.findByGenderContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on gender field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByGenderContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(studentRepository.findByPhoneNoContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on phone_no field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByPhoneNoContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(studentRepository.findByCurrentStatusContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on current_status field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByCurrentStatusContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(studentRepository.findByCourseContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on course field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByCourseContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(studentRepository.findByBatchContains(value, PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on batch field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByBatchContains(value, PageRequest.of(pageNo,pageSize));
    
    	}else if(isNumeric(value) && studentRepository.findByAgeEquals(Integer.parseInt(value), PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on age field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByAgeEquals(Integer.parseInt(value),PageRequest.of(pageNo,pageSize));
    
    	}else if(isNumeric(value) && studentRepository.findByFeesEquals(Integer.parseInt(value), PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on fees field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByFeesEquals(Integer.parseInt(value),PageRequest.of(pageNo,pageSize));
    
    	}else if(isNumeric(value) && studentRepository.findByClassIdEquals(Integer.parseInt(value), PageRequest.of(0,1)).size() > 0) {
    		
    		logger.info("Searched Keyword matched on class_id field.");
    		logger.info("Student Details returned by Searching successfully.");
    		return studentRepository.findByClassIdEquals(Integer.parseInt(value),PageRequest.of(pageNo,pageSize));
    
    	}else {
    		
    		logger.info("No Student found for searched Keyword matched");
    		Student emptyStudent = new Student();
    		emptyStudent.setName("No Student Found");
    		List<Student> students =  new ArrayList<>();
    		students.add(emptyStudent);
    		return students;
    	}
 	}
    
    public Page<Student> findStudentDetailsWithFilter(StudentFilter filter, int pageNo, int pageSize){
    	
		logger.info("Student Details returned by Filtering successfully.");
    	Specification<Student> spec = FilteredStudents.filterBy(filter);
        Page<Student> students = studentRepository.findAll(spec, PageRequest.of(pageNo, pageSize));
    	
    	return students;
    }
    
    public static boolean isNumeric(String str) { 
    	
    	try {  
    		Double.parseDouble(str);  
    	  	return true;
    	} catch(NumberFormatException e){  
    		return false;  
    	}  
    }
    
    private String validate(Student student, boolean bool){
    	
    	logger.info("Validating Input Data");
    	
		if(allStudents.checkRegisterNo(student.getRegisterNo(), student.getStudentId()) ) 
		{
			
			logger.info("Entered Register number for student name: "+ student.getName()+" is "+ student.getRegisterNo());
			logger.warn("This Register number is already registered but register number should be unique");
			return "Register number is already registered";
		}	
		
		else if(allStudents.checkEmailId(student.getEmailId(),student.getStudentId())) 
		{
			
			logger.info("Entered Email Id for student name: "+ student.getName()+" is "+ student.getEmailId());
			logger.warn("This Email Id is already registered");
			return "Email Id is already registered";
		
		}
		
		else if(!student.getGender().equalsIgnoreCase("female") && !student.getGender().equalsIgnoreCase("male")) 
		{
		
			logger.info("Entered Gender for student name: "+ student.getName()+" is "+ student.getGender());
			logger.warn("Gender should be either male or female");
			return "Invalid Gender for input: "+ student.getGender();
		}
		else if(student.getPhoneNo().length() !=10 ) 
		{
			
			logger.info("Entered Phone number for student name: "+ student.getName()+" is "+ student.getPhoneNo());
			logger.warn("Phone number should be 10 digits long");
			return "Phone number must be 10 digits.";
		}	
		else if( !student.getName().matches("[a-zA-Z ]+") || student.getName().length() == 0) 
		{
			
			logger.warn("Invalid value for name field: "+ student.getName());
			return "Invalid value for name field: "+ student.getName();
		
		}
		else if(student.getEmailId() == null || student.getEmailId().length() == 0)
		{
				
			logger.warn("Email field cannot be empty");
			return "Email field cannot be empty.";
			
		}
		else if(!(student.getRegisterNo()+"").matches("[0-9]+") || student.getRegisterNo() == 0) 
		{
			
			logger.warn("Invalid value for register no: "+ student.getRegisterNo());
			return "Invalid value for register no: "+ student.getRegisterNo();
			
		}
		else if(!(student.getAge()+"").matches("[0-9]+") || student.getAge() == 0)
		{
				
			logger.warn("Invalid value for age: "+ student.getAge());
			return "Invalid value for age: "+ student.getAge();
		}
		else if(!(student.getPhoneNo()).matches("[0-9]+")) 
		{
			
			logger.warn("Invalid value for phone no: "+ student.getPhoneNo());
			return "Invalid value for phone no: "+ student.getPhoneNo();
		}
		else if(!(student.getFees()+"").matches("[0-9]+") || student.getFees() == 0) 
		{
			
			logger.warn("Invalid value for Fees: "+ student.getFees());
			return "Invalid value for Fees: "+ student.getFees();
		}
		else if(!(student.getClassId()+"").matches("[0-9]+") || student.getClassId() == 0) 
		{
			
			logger.warn("Invalid value for classId: "+ student.getClassId());
			return "Invalid value for classId: "+ student.getClassId();
		}
		else if(!student.getCurrentStatus().equalsIgnoreCase("Available") && !student.getCurrentStatus().equalsIgnoreCase("Busy") && !student.getCurrentStatus().equalsIgnoreCase("Passout")) 
		{
		
			logger.warn("Invalid value for current status: "+ student.getCurrentStatus());
			return "Invalid Current Status: " + student.getCurrentStatus();
		}
		else if( !student.getCourse().matches("[a-zA-Z ]+") || student.getCourse().length() == 0) 
		{
			
			logger.warn("Invalid value for Course field: "+ student.getCourse());
			return "Invalid value for course field: "+ student.getCourse();
		
		}
		else if(!(student.getBatch()).matches("[0-9- ]+")) 
		{
			
			logger.warn("Invalid value for batch: "+ student.getBatch());
			return "Invalid value for batch: "+ student.getBatch();
		}else {
			
			logger.info("Provided Data is Valid");
			return "valid";
		}
    }
    
}
