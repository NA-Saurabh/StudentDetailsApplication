package com.example.StudentDetails;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.StudentDetails.DAO.StudentDetailsDAO;
import com.example.StudentDetails.Model.Student;
import com.example.StudentDetails.Service.StudentDetailsService;

@SpringBootTest
class StudentDetailsApplicationTests {
	
	@Autowired
	StudentDetailsDAO studentDao;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Test
	void findAllStudentTest(){
		String sql = "SELECT * FROM `student` where isDeleted = 'No'";
		RowMapper<Student> row = new BeanPropertyRowMapper<>(Student.class);
		
		List<Student> expected = jdbc.query(sql,row);
		
		List<Student> actual = studentDao.findAllStudents();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

	}
	
	@Test
	void findStudentByIdTest(){
		Student expected = new Student(1,"John Doe",123401,"Male",25,"john@gmail.com","9234567890","Available","CSE","2024-28",100000,1);
		
		Student actual = studentDao.findStudentById(1);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

	}
	
	@Test
	void addStudentWithError(){
		
		Student student = new Student(1,"John Doe",123401,"Male",25,"john@gmail.com","9234567890","Available","CSE","2024-28",100000);
		
		int actual = studentDao.addStudent(student);
		
		assertEquals(0,actual);

	}
	
	@Test
	void addStudent(){
		
		Student student = new Student(0,"David Warner",123416,"Male",25,"david@gmail.com","9234567890","Available","CSE","2024-28",100000);
		
		int actual = studentDao.addStudent(student);
		
		assertEquals(0,actual);

	}
	
	@Test
	void updateStudentByIdTestError(){
		
		Student updatedStudent = new Student(1,"John Doe",123401,"Male",25,"john@gmail.com","9234567890","Available","CSE","2024-28",100000,1);
		
		int actual = studentDao.updateStudentDetails(1,updatedStudent);
		
		assertEquals(0,actual);

	}
	
	@Test
	void updateStudentByIdTest(){
		
		Student updatedStudent = new Student(1,"John Doe",123401,"Male",25,"john@gmail.com","9234567890","Available","CSE","2024-28",100000,1);
		
		int actual = studentDao.updateStudentDetails(1,updatedStudent);
		
		assertEquals(0,actual);

	}
	
	@Test
	void deleteStudentByIdTestError(){
				
		int actual = studentDao.deleteStudentById(1000);
		
		assertEquals(0,actual);

	}
	
	@Test
	void checkRegisterNo(){
		
		boolean expected = true;
		
		boolean actual = studentDao.checkRegisterNo(123401,0);
		
		assertEquals(expected, actual);

	}
	
	@Test
	void checkRegisterNoError(){
		
		boolean expected = false;
		
		boolean actual = studentDao.checkRegisterNo(1,0);
		
		assertEquals(expected,actual);

	}
	
	@Test
	void checkRegisterNoWithID(){
		
		boolean expected = true;
		
		boolean actual = studentDao.checkRegisterNo(123402,1);
		
		assertEquals(expected, actual);

	}
	
	@Test
	void checkRegisterNoWithIDError(){
		
		boolean expected = false;
		
		boolean actual = studentDao.checkRegisterNo(1,1);
		
		assertEquals(expected,actual);

	}
	
	@Test
	void checkEmailId(){
		
		boolean expected = true;
		
		boolean actual = studentDao.checkEmailId("john@gmail.com",0);
		
		assertEquals(expected, actual);

	}
	
	@Test
	void checkEmailIDError(){
		
		boolean expected = false;
		
		boolean actual = studentDao.checkEmailId("saurabh@gmail.com",0);
		
		assertEquals(expected,actual);

	}
	
	@Test
	void checkEmailIdWithStudentID(){
		
		boolean expected = true;
		
		boolean actual = studentDao.checkEmailId("ben.s@gmail.com",1);
		
		assertEquals(expected, actual);

	}
	
	@Test
	void checkEmailIdWithStudentIDError(){
		
		boolean expected = false;
		
		boolean actual = studentDao.checkEmailId("saurabh@gmail.com",1);
		
		assertEquals(expected,actual);

	}
	
	@Test
	void checkForStudent(){
		
		int actual = studentDao.checkForStudent(1);
		
		assertEquals(1,actual);

	}
	
	
}
