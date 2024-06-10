package com.example.StudentDetails.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.StudentDetails.DAO.QuestionsDAO;
import com.example.StudentDetails.Model.Question;

@Repository
public class QuestionsDAOImpl implements QuestionsDAO{
	
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public List<Question> getAllQuestions() {
		String sql = "SELECT * FROM Questions;";
		RowMapper<Question> row = new BeanPropertyRowMapper<>(Question.class);
		List<Question> result = jdbc.query(sql,row);
		
		return result;
	}

	@Override
	public Question findQuestionById(int question_id) {
		String sql = "SELECT question_id,class_id,question,answer FROM Questions WHERE question_id = ?";
		RowMapper<Question> row = new BeanPropertyRowMapper<>(Question.class);
		Question result = jdbc.queryForObject(sql,row ,question_id);
		return result;
	}
	
	@Override
	public List<Question> findQuestionByClassId(int class_id) {
		String sql = "SELECT question_id,class_id,question,answer FROM Questions WHERE class_id = ?";
		RowMapper<Question> row = new BeanPropertyRowMapper<>(Question.class);
		List<Question> result = jdbc.query(sql,row ,class_id);
		return result;
	}

	@Override
	public int addQuestion(Question Question) {
		String sql = "INSERT INTO Questions(class_id, question, answer) values(?,?,?)";
		int result = jdbc.update(sql,Question.getClass_id(),Question.getQuestion(),Question.getAnswer());
		
		return result;
	}

	@Override
	public int updateQuestion(int question_id, Question question) {
		String sql="UPDATE Questions SET class_id = ?, question = ?, answer =?  WHERE question_id = ?";
		int result=jdbc.update(sql,question.getClass_id(),question.getQuestion(),question.getAnswer(),question_id);
		
		return result;
	}

	@Override
	public int deleteQuestionById(int question_id) {
		int result = 0;
		String sql = "DELETE FROM Questions WHERE question_id=?";
		result = jdbc.update(sql, question_id);
				
		return result;
	}


}
