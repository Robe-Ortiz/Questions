package com.robe_ortiz_questions.entity.game;

import java.util.List;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;

public class Game {

	private int numberOfQuestions;
	private CategoryOfQuestion categoryOfQuestions;
	private List<Question> listOfQuestions;

	public Game(int numberOfQuestions, CategoryOfQuestion categoryOfQuestions, List<Question> listOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
		this.categoryOfQuestions = categoryOfQuestions;
		this.listOfQuestions = listOfQuestions;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public CategoryOfQuestion getCategoryOfQuestions() {
		return categoryOfQuestions;
	}

	public List<Question> getListOfQuestions() {
		return listOfQuestions;
	}
		
}
