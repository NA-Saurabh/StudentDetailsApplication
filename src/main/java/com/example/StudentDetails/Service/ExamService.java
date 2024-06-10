package com.example.StudentDetails.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.StudentDetails.DAO.ExamDAO;
import com.example.StudentDetails.Model.ErrorResponse;
import com.example.StudentDetails.Model.Exam;


@Service
public class ExamService {
	
	@Autowired
	private ExamDAO Exam;
	
	public ResponseEntity<?> startExam(Exam exam){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		try {
			Date start = dateFormat.parse(exam.getStart_time());
			ZoneId zone = ZoneId.of("Asia/Kolkata");  
		    LocalTime currentTime =  LocalTime.now(zone);

			if (start.getHours() <  currentTime.getHour() || start.getHours() == currentTime.getHour() &&
					start.getMinutes() < currentTime.getMinute()) {
				
				ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST,"Exam start time is in the past");
				return ResponseEntity.badRequest().body(response);	
			}else {
				
				long delay = parseDuration(exam.getDuration());
				Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                calendar.add(Calendar.MILLISECOND, (int) delay);  // Adding delay in milliseconds
                Date end = calendar.getTime();
                String exam_end = dateFormat.format(end);
                
                Exam newExam = new Exam();
                newExam.setClass_id(exam.getClass_id());
                newExam.setStart_time(exam.getStart_time());
                newExam.setEnd_time(exam_end);
                newExam.setDuration(exam.getDuration());
                		
				int result = Exam.startExam(exam);
				
				if(result == 0) {
					
					return new ResponseEntity<>("Failed to start Exam",HttpStatus.NOT_FOUND);
			    }else {
			    	Exam.scheduleEndExam(newExam, delay);
			    	ErrorResponse response = new ErrorResponse(HttpStatus.OK,"Exam Started");
					return ResponseEntity.ok().body(response);	    
			    }
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);	
			
		}
	
	}
	
	private long parseDuration(String duration) {
	    
	    String[] parts = duration.split(":");
	    int hours = Integer.parseInt(parts[0].trim());
	    
	    int minutes = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim());

	    long hoursInMillis = hours * 60 * 60 * 1000;
	    long minutesInMillis = minutes * 60 * 1000;

	    return hoursInMillis + minutesInMillis;
	}
}
