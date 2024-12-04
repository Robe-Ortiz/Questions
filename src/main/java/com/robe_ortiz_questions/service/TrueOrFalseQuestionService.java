package com.robe_ortiz_questions.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robe_ortiz_questions.entity.TrueOrFalseQuestion;
import com.robe_ortiz_questions.repository.TrueOrFalseQuestionRepository;

@Service
public class TrueOrFalseQuestionService {

	@Autowired
	private TrueOrFalseQuestionRepository trueOrFalseQuestionRepository;
	
	public List<TrueOrFalseQuestion> findAll(){
		return trueOrFalseQuestionRepository.findAll();
	}
}
