package com.example.StudentDetails.Repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.StudentDetails.Model.LoginRequest;
import com.example.StudentDetails.Model.User;

@Repository
public class UserRepository {
	
	@Autowired
	JdbcTemplate jdbc;
	
    public User findUserByEmail(String email){
    	
    	String sql="Select user_id, first_name, last_name, email, password from `User` where email=?";
        RowMapper<User> row = new BeanPropertyRowMapper<User>(User.class);
        User user = jdbc.queryForObject(sql, row, email);
        return user;
    }
    
    public User findById(int id){
    	
    	String sql="Select user_id, first_name, last_name, email, password from `User` where user_id=?";
        RowMapper<User> row = new BeanPropertyRowMapper<User>(User.class);
        User user = jdbc.queryForObject(sql, row, id);
        return user;
    }
    
    public int addUser(User user){
    	
    	String sql="INSERT INTO User (first_name, last_name, email, password) VALUES (?,?,?,?);";
        int result = jdbc.update(sql,user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return result;
    }
}