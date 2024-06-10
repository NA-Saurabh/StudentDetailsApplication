package com.example.StudentDetails.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.StudentDetails.Service.ExamService;
import com.example.StudentDetails.Model.Exam;


@RestController
public class ExamController {
	
	@Autowired
	ExamService Exam;
	
    @PostMapping("/startExam")
	public ResponseEntity<?> startExam(@RequestBody Exam exam){
    	
		return Exam.startExam(exam);
    	
	}
	
}
