package com.example.StudentDetails.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.StudentDetails.DAO.QuestionsDAO;
import com.example.StudentDetails.Model.Question;
import com.example.StudentDetails.Model.Student;

@Service
public class QuestionService {
	
	@Autowired
	private QuestionsDAO Questions;

	public List<Question> getAllQuestions(){
		
		return Questions.getAllQuestions();
	}

	public ResponseEntity<?> findQuestionById(int question_id) {
	    Question matchedQuestion = null;
	    try {
	        matchedQuestion = Questions.findQuestionById(question_id);
	        if (matchedQuestion == null) {
	            return new ResponseEntity<String>("No Question found with this Question ID", HttpStatus.NOT_FOUND);
	        }
	    } catch (EmptyResultDataAccessException e) {
	        return new ResponseEntity<String>("No Question found with this Question ID", HttpStatus.NOT_FOUND);
	    }

	    return new ResponseEntity<Question>(matchedQuestion, HttpStatus.OK);
	}
	
	public ResponseEntity<?> findQuestionByClassId(int class_id) {
		if(class_id > 0 && class_id <= 10 ) {
		    try {
		        List<Question> matchedQuestions = Questions.findQuestionByClassId(class_id);
	
		        if (matchedQuestions.isEmpty()) {
		            String error = "No Question Found";
		            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		        }
	
		        return new ResponseEntity<>(matchedQuestions, HttpStatus.OK);
		    } catch (EmptyResultDataAccessException e) {
		        String error = "No Question Found";
		        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		    }
		}else {
			String error = "Invalid Class ID";
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}

	
    public ResponseEntity<String> addQuestion(Question question) {
    	
    	int result = Questions.addQuestion(question);
		
    	if(result == 1) {
    		
    		return new ResponseEntity<>("Question Added Successfully",HttpStatus.OK);
    	}else {
    		return new ResponseEntity<>("Something Went Wrong",HttpStatus.NOT_ACCEPTABLE);
    	}
	}
    
    public ResponseEntity<String> updateQuestion(int question_id, Question question) {
    	
    	int result = Questions.updateQuestion(question_id,question);
		
    	if(result == 1) {
    		
    		return new ResponseEntity<>("Question Updated Successfully",HttpStatus.OK);
    	}else {
    		return new ResponseEntity<>("No Question Found",HttpStatus.NOT_ACCEPTABLE);
    	}
	}
    
    public ResponseEntity<String> deleteQuestion(int question_id) {
    	
    	int result = Questions.deleteQuestionById(question_id);
		
    	if(result == 1) {
    		
    		return new ResponseEntity<>("Question Deleted Successfully",HttpStatus.OK);
    	}else {
    		return new ResponseEntity<>("No Question Found",HttpStatus.NOT_ACCEPTABLE);
    	}
	}
}
