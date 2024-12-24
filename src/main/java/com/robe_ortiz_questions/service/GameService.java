package com.robe_ortiz_questions.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.repository.QuestionRepository;



@Service
public class GameService {

	@Autowired
	private QuestionRepository questionRepository;
		
	public List<Question> getQuestions(int numberOfQuestions, CategoryOfQuestion categoryOfQuestions){
		List<Question> listOfQuestions = questionRepository.findByCategory(categoryOfQuestions);
		Collections.shuffle(listOfQuestions);
		return listOfQuestions.stream().limit(numberOfQuestions).toList();
	}
}
