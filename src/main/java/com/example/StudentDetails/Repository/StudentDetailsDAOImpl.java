package com.example.StudentDetails.Repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.StudentDetails.DAO.StudentDetailsDAO;
import com.example.StudentDetails.Model.Student;

@Repository
public class StudentDetailsDAOImpl implements StudentDetailsDAO{
	
	Logger logger = LoggerFactory.getLogger(StudentDetailsDAOImpl.class);
	
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Student> findAllStudents() {
		
		String sql = "SELECT * FROM student where isDeleted = 'No'";
		RowMapper<Student> row = new BeanPropertyRowMapper<Student>(Student.class);
		List<Student> studentData = jdbc.query(sql,row);
		logger.info("All Student Details Retrieved");
		return studentData;
	}

	@Override
	public Student findStudentById(int student_id) {
		String sql="Select student_id,name,register_no,age,gender,email_id,phone_no,current_status,batch,course,fees,class_id from student where student_id=? and isDeleted = 'No'";
        RowMapper<Student> row = new BeanPropertyRowMapper<Student>(Student.class);
        Student matchedStudent = jdbc.queryForObject(sql, row, student_id);
        logger.info("Student Details Retrieved with the student_id: "+ student_id);
		return matchedStudent;
	}
	
	@Override
	public int addStudent(Student studentDetails) {
		
		int result = 0;
			
		String sql = "INSERT INTO student (name, register_no, gender, age, phone_no, current_status, email_id, batch, course, fees, class_id) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
			
		result = jdbc.update(sql,studentDetails.getName(), studentDetails.getRegisterNo(), studentDetails.getGender(), studentDetails.getAge(), studentDetails.getPhoneNo(),studentDetails.getCurrentStatus(),
					studentDetails.getEmailId(), studentDetails.getBatch(), studentDetails.getCourse(), studentDetails.getFees(), studentDetails.getClassId());
		
		logger.info("Student Added with student_name: " + studentDetails.getName() );
		return result;
	}

	@Override
	public int updateStudentDetails(int student_id, Student studentDetails) {
		
		int result = 0;
		
		Student existingStudent = findStudentById(student_id);
		
		if(studentDetails.equals(existingStudent)) {
			
			return result;
		
		}else {
			
			String sql="UPDATE student SET name = ?, gender = ? , age = ? ,phone_no = ?, current_status = ?,batch = ?,course = ?,fees = ?, class_id = ? WHERE student_id = ? and isDeleted='No'";	
			result=jdbc.update(sql,studentDetails.getName(),studentDetails.getGender(),studentDetails.getAge(),studentDetails.getPhoneNo(),studentDetails.getCurrentStatus(),
					           studentDetails.getBatch(),studentDetails.getCourse(),studentDetails.getFees(),studentDetails.getClassId(),student_id);
			return result;
		}
	}

	@Override
	public int deleteStudentById(int student_id) {
		int result = 0;
		String sql = "UPDATE student SET isDeleted = 'Yes' WHERE student_id=?;";
		result = jdbc.update(sql, student_id);
				
		logger.info("Student Deleted with student_id: " + student_id );
		return result;
	}
    
	@Override
	public boolean checkRegisterNo(int register_no, int studentId) {
		
		if(studentId == 0) {
			
			logger.info("Checking register_no: " + register_no );
			int result;
			String sql="select count(register_no) from student where register_no=? and isDeleted = 'No'";
			result = jdbc.queryForObject(sql,Integer.class ,register_no);
			
		    if(result == 0) {
		    	
		    	logger.info( register_no + " is unique.");
		    	return false;
		    }else {
		    	
		    	logger.info( register_no + " is already registered. ");
		    	return true;
		    }
		}else {
			
			logger.info("Checking register_no: " + register_no );
			int result;
			String sql="select count(register_no) from student where register_no=? and isDeleted = 'No' and student_id != ?";
			result = jdbc.queryForObject(sql,Integer.class ,register_no, studentId);
			
		    if(result == 0) {
		    	
		    	logger.info( register_no + " is unique.");
		    	return false;
		    }else {
		    	
		    	logger.info( register_no + " is already registered. ");
		    	return true;
		    }
		}
		

	}
	
	@Override
	public boolean checkEmailId(String email, int studentId) {
		
		if (studentId == 0) {
			
			logger.info("Checking for Email: " + email );
			int result;
			String sql="select count(email_id) from student where email_id=? and isDeleted = 'No'";
			result = jdbc.queryForObject(sql,Integer.class ,email);
			
		    if(result == 0) {
		    	
		    	logger.info( email + " is unique.");
		    	return false;
		    }else {
		    	
		    	logger.info( email + " is already registered. ");
		    	return true;
		    }
		}else {
			
			logger.info("Checking for Email: " + email );
			int result;
			String sql="select count(email_id) from student where email_id=? and isDeleted = 'No' and student_id != ?";
			result = jdbc.queryForObject(sql,Integer.class ,email, studentId);
			
		    if(result == 0) {
		    	
		    	logger.info( email + " is unique.");
		    	return false;
		    }else {
		    	
		    	logger.info( email + " is already registered. ");
		    	return true;
		    }
		}
	}

	@Override
	public int checkForStudent(int studentId) {
		
		String sql = "select count(register_no) from student where student_id=? and isDeleted = 'No'";
		int result = jdbc.queryForObject(sql,Integer.class ,studentId);
		
		return result;
	}
}