package com.example.StudentDetails.Model;

public class StudentFilter {
	
	private String course;
	private String batch;
	private String name;
	private String phoneNo;
	
	public StudentFilter(String course, String batch, String name, String phoneNo) {
		super();
		this.course = course;
		this.batch = batch;
		this.name = name;
		this.phoneNo = phoneNo;
	}
	
	public StudentFilter() {

	}

	
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
