package com.example.StudentDetails.Model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Question {
	
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int question_id;
	private int class_id;
	private String question;
	private String answer;
	
	

	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	

}
