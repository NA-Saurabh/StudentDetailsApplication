package com.example.StudentDetails.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.StudentDetails.DAO.StudentDetailsDAO;
import com.example.StudentDetails.Helper.ExcelHelper;
import com.example.StudentDetails.Model.ClassModel;
import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Repository.ClassRepository;
import com.example.StudentDetails.Repository.StudentDetailsRepository;

@Service
public class ExcelService {

	
	@Autowired
	StudentDetailsDAO studentRepo;
	
	@Autowired
	StudentDetailsRepository repository;
	
	@Autowired
	ClassRepository classRepository;

	public ByteArrayInputStream load(int student_id) {
	  
		if(student_id == 0) {
		  
			List<Student> students = repository.findAll();
			
			ByteArrayInputStream in = ExcelHelper.studentsToExcel(students);
			return in;
	 
		}else {
		  
			Student student = repository.findByStudentId(student_id);
			List<Student> students = new ArrayList<>();
			students.add(student);
			
			ByteArrayInputStream in = ExcelHelper.studentsToExcel(students);
			return in;
		}  
	}
	
	public String save(MultipartFile file) throws Exception {
		
		try {
			List<Student> students = ExcelHelper.excelToStudent(file.getInputStream());
			String isValid = isValid(students);
			
			if (isValid.equalsIgnoreCase("valid")) {
				for(Student student : students) {
					
					System.out.print("Outside that");
					if(studentRepo.checkForStudent(student.getStudentId()|0) == 0) {
						
						if (!studentRepo.checkRegisterNo(student.getRegisterNo(),0) && !studentRepo.checkEmailId(student.getEmailId(),0)) {
							
							System.out.println("Insside that");
							studentRepo.addStudent(student);
						}else if(studentRepo.checkRegisterNo(student.getRegisterNo(),0)){
							
							throw new Exception("Duplicate Register Number");
						}else {
							
							throw new Exception("Duplicate Email ID");
						}
						
						
					}else {
						
						continue;
					}	
				}
				return "Data Saved to DB";
			
			}else {
				
				return "invalid";
			}
            
	    
		} catch (IOException e) {
	    	
	    	throw new RuntimeException("fail to store excel data: " + e.getMessage());
	    }
	}
	
	public ByteArrayInputStream loadClassData() {
		  
		  
		List<ClassModel> classes = classRepository.findAll();
			
		ByteArrayInputStream in = ExcelHelper.classStudentsToExcel(classes);
		return in;

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