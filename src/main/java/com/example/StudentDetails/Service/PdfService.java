package com.example.StudentDetails.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Repository.StudentDetailsRepository;

@Service
public class PdfService {
	
	@Autowired
	StudentDetailsRepository studentRepository;
	
	public List<Student> findAllStudents(){
		
		return studentRepository.findAll();
	}
	
    public Student findStudentById(int student_id){
		
		return studentRepository.findByStudentId(student_id);
	}
}