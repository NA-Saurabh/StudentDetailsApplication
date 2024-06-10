package com.example.StudentDetails.DAO;


import java.util.concurrent.CompletableFuture;

import com.example.StudentDetails.Model.Exam;


public interface ExamDAO {
	
	public int startExam(Exam exam);
	public CompletableFuture<Void> scheduleEndExam(Exam exam,long delay);
	
}
