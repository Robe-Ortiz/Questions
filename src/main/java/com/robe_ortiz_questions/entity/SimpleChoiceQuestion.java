package com.robe_ortiz_questions.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
public class SimpleChoiceQuestion extends Question {

	@ElementCollection
	   @CollectionTable(
		        name = "question_answers"
		    )
    private List<String> answers;

    private String correctAnswer;
    
    public SimpleChoiceQuestion() {}
               
	public SimpleChoiceQuestion(Long id, String question, CategoryOfQuestion category, List<String> answers, String correctAnswer) {
		super(id, question, category);
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
    
	@Override
	public TypeOfQuestion getTipoDePregunta() {
		return TypeOfQuestion.SINGLE_CHOICE;
	} 
}
