package com.example.StudentDetails.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class dataBaseTokenBlacklist implements TokenBlacklist {

	@Autowired
    JdbcTemplate jdbc;

    @Override
    public void addToBlacklist(String token) {
       
    	String sql = "INSERT INTO tokenBlacklist (token) VALUES (?);";
    	int result = jdbc.update(sql, token);
    	
    }

    @Override
    public boolean isBlacklisted(String token) {
       
    	String sql = "SELECT COUNT(*) from tokenBlacklist WHERE token = ?;";
    	int count = jdbc.queryForObject(sql, Integer.class, token);
    	
    	if (count == 0) {
    		return false;
    	}else {
    		return true;
    	}	
    }
}

