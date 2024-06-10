package com.example.StudentDetails.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.StudentDetails.Model.Question;
import com.example.StudentDetails.Service.QuestionService;

@RestController
public class QuestionController {
	
	@Autowired
	private QuestionService Questions;
	
	@GetMapping("/getAllQuestions")
	public List<Question> getAllQuestions(){
		
		return Questions.getAllQuestions();
	}
	
	@GetMapping("/getQuestion/{question_id}")
	public ResponseEntity<?> findQuestionById(@PathVariable int question_id){
		
		return Questions.findQuestionById(question_id);
	}
	
	@GetMapping("/getQuestion")
	public ResponseEntity<?> findQuestionByClassId(@RequestParam int class_id){
		
		return Questions.findQuestionByClassId(class_id);
	}
	
	@PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
		
		return Questions.addQuestion(question);
	}
	
	@PutMapping("/updateQuestion/{question_id}")
    public ResponseEntity<String> updateQuestion(@PathVariable int question_id,@RequestBody Question question) {
		
		return Questions.updateQuestion(question_id,question);
	}
	
	@DeleteMapping("/deleteQuestion/{question_id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable int question_id) {
		
		return Questions.deleteQuestion(question_id);
	}

}
