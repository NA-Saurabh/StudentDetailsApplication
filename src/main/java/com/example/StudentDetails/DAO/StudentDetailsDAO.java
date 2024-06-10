package com.example.StudentDetails.DAO;

import java.util.List;

import com.example.StudentDetails.Model.Student;

public interface StudentDetailsDAO {
	
	public List<Student> findAllStudents();
	
	public Student findStudentById(int student_id);
	
	public int addStudent(Student studentDetails);
	
	public int updateStudentDetails(int student_id,Student studentDetails);
	
	public int deleteStudentById(int student_id);
	
	public boolean checkRegisterNo(int register_no, int studentId);
	
	public boolean checkEmailId(String emailId, int studentId);
	
	public int checkForStudent(int studentId);

}
