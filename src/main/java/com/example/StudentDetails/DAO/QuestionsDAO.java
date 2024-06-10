package com.example.StudentDetails.DAO;

import java.util.List;

import com.example.StudentDetails.Model.Question;

public interface QuestionsDAO {
	
    public List<Question> getAllQuestions();
	
	public Question findQuestionById(int question_id);
	
	public List<Question> findQuestionByClassId(int class_id);
	
	public int addQuestion(Question question);
	
	public int updateQuestion(int question_id,Question question);
	
	public int deleteQuestionById(int question_id);

}
