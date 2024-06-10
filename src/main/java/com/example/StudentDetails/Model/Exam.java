package com.example.StudentDetails.Model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Exam {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int exam_id;
	private int class_id;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int student_id;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int assignment_id;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int grade;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int question_id;
	private String start_time;
	private String end_time;
	private String duration;
	private String examName;
	
	
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(int assignment_id) {
		this.assignment_id = assignment_id;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}

	
}