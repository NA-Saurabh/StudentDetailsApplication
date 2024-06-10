package com.example.StudentDetails.Model;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "student")
public class Student {

	@Id
	@GeneratedValue
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private int studentId;
	public String name;
    private int registerNo;
    private String gender;
    private int age;
    private String emailId;
    private String phoneNo;
    private String currentStatus;
    private String course;
    private String batch;
    private int fees;
    private int classId;
    
    public Student() {
    }
    
    public Student(int studentId, String name, int registerNo, String gender, int age, String emailId,
			String phoneNo, String currentStatus, String course, String batch, int fees, int classId) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.registerNo = registerNo;
		this.gender = gender;
		this.age = age;
		this.emailId = emailId;
		this.phoneNo = phoneNo;
		this.currentStatus = currentStatus;
		this.course = course;
		this.batch = batch;
		this.fees = fees;
		this.classId = classId;
	}
    
    public Student(int studentId, String name, int registerNo, String gender, int age, String emailId,
			String phoneNo, String currentStatus, String course, String batch, int fees) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.registerNo = registerNo;
		this.gender = gender;
		this.age = age;
		this.emailId = emailId;
		this.phoneNo = phoneNo;
		this.currentStatus = currentStatus;
		this.course = course;
		this.batch = batch;
		this.fees = fees;
	}
    
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(int registerNo) {
		this.registerNo = registerNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
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
	public int getFees() {
		return fees;
	}
	public void setFees(int fees) {
		this.fees = fees;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", name=" + name + ", registerNo=" + registerNo + ", gender="
				+ gender + ", age=" + age + ", emailId=" + emailId + ", phoneNo=" + phoneNo + ", currentStatus="
				+ currentStatus + ", course=" + course + ", batch=" + batch + ", fees=" + fees + ", classId=" + classId
				+ "]";
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;
        return registerNo == student.registerNo &&
                age == student.age &&
                Double.compare(student.fees, fees) == 0 &&
                classId == student.classId &&
                Objects.equals(name, student.name) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(emailId, student.emailId) &&
                Objects.equals(phoneNo, student.phoneNo) &&
                Objects.equals(currentStatus, student.currentStatus) &&
                Objects.equals(course, student.course) &&
                Objects.equals(batch, student.batch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, registerNo, gender, age, emailId, phoneNo, currentStatus, course, batch, fees, classId);
    }
    
}