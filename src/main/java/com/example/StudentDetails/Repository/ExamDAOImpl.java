package com.example.StudentDetails.Repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.StudentDetails.DAO.ExamDAO;
import com.example.StudentDetails.Model.Exam;

@Repository
public class ExamDAOImpl implements ExamDAO {
	
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public int startExam(Exam exam)  {
		
		String sql = "UPDATE student SET current_status = 'Busy' where class_id = ?";
		int result = jdbc.update(sql, exam.getClass_id());
		
		String insertExam = "INSERT into Exam (class_id,exam_name, start_time, end_time, duration) VALUES (?,?,?,?,?)";
        jdbc.update(insertExam, exam.getClass_id(),exam.getExamName(), exam.getStart_time(), exam.getEnd_time(), exam.getDuration());
         
		
		return result;
	}
	
    public CompletableFuture<Void> scheduleEndExam(Exam exam, long delay) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(delay);
                String sql = "UPDATE student SET current_status = 'Available' where class_id = ?";
                jdbc.update(sql, exam.getClass_id());
               
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            	e.printStackTrace();
            }
        });
    }
}
