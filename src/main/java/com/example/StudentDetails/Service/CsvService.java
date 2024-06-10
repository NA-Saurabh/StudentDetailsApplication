package com.example.StudentDetails.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.DAO.StudentDetailsDAO;
import com.example.StudentDetails.Helper.CsvHelper;
import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Repository.StudentDetailsRepository;

@Service
public class CsvService {

	@Autowired
	StudentDetailsDAO studentRepo;
	
	@Autowired
	StudentDetailsRepository repository;
  
	public ByteArrayInputStream load() {
		
		List<Student> students = repository.findAll();

		ByteArrayInputStream in = CsvHelper.studentsToCSV(students);
		return in;
	}
	
	public ByteArrayInputStream loadById(int student_id) {
		
		List<Student> students =  new ArrayList<>();
		Student student= repository.findByStudentId(student_id);
		students.add(student);

		ByteArrayInputStream in = CsvHelper.studentsToCSV(students);
		return in;
	}
	
	public String save(MultipartFile file) throws Exception {	
	    try {
	    	
	    	System.out.println("helper is called");
	    	List<Student> students = CsvHelper.csvToStudents(file.getInputStream());
	    	String validation = isValid(students);
			
	    	System.out.println("checks for validation");
			if (validation.equalsIgnoreCase("valid")) {
				int count = 0;
				
				System.out.println("validation passed");
				for(Student student : students) {
					
					if(!studentRepo.checkRegisterNo(student.getRegisterNo(),0)) {
						
						if(!studentRepo.checkEmailId(student.getEmailId(),0)) {
							
							int result = studentRepo.addStudent(student);
							count +=result ;
							
						}else {
							
							throw new Exception("Duplicate Email Id");
						}	
						
					}else {
						
						throw new Exception("Duplicate Register Number");
					}	
				}
				
				if (count == students.size()) {
					
					return "Data updated successfully";
				}else {
					
					return "Something went wrong";
				}
				
			
			}else {
				
				System.out.println("validation failed");
				return "invalid";
			}
	    
	    } catch (IOException e) {
	    	
	    	throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	}
	
	public String isValid(List<Student> students) {
		
		for(Student student : students) {
			
			if(!student.getGender().equalsIgnoreCase("male") && !student.getGender().equalsIgnoreCase("female")) {
				
				return "invalid";
				
			}else if(student.getPhoneNo().length() != 10) {
				
				return "invalid";
				
			}else {
				continue;
			}
			
		}

		return "valid";
	}
}