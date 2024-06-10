package com.example.StudentDetails.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.StudentDetails.Model.ClassModel;
import com.example.StudentDetails.Model.Student;

@Repository
public class ClassRepository{
	
	@Autowired
	JdbcTemplate jdbc;
	
	public List<ClassModel> findAll(){
		String sql = "WITH RankedStudents AS (\n"
				+ "    SELECT \n"
				+ "        c.class_id AS class_id_c,\n"
				+ "        s.student_id,\n"
				+ "        s.name,\n"
				+ "        s.register_no,\n"
				+ "        ROW_NUMBER() OVER (PARTITION BY c.class_id ORDER BY s.student_id) AS RowNum\n"
				+ "    FROM Class c\n"
				+ "    JOIN student s ON c.class_id = s.class_id\n"
				+ ")\n"
				+ "SELECT \n"
				+ "    CASE WHEN RowNum = 1 THEN class_id_c ELSE NULL END AS class_id,\n"
				+ "    rs.student_id,\n"
				+ "    rs.name,\n"
				+ "    rs.register_no,\n"
				+ "    e.exam_id,\n"
				+ "    e.exam_name\n"
				+ "FROM RankedStudents rs\n"
				+ "LEFT JOIN Exam e ON e.class_id = rs.class_id_c\n"
				+ "ORDER BY rs.class_id_c;\n";
		
		RowMapper<ClassModel> row = new BeanPropertyRowMapper<ClassModel>(ClassModel.class);
		List<ClassModel> classData = jdbc.query(sql,row);
		return classData;
				
	}
	
}
