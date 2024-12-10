package com.robe_ortiz_questions.entity;

import java.util.List;
import jakarta.persistence.Entity;

@Entity
public class MultipleChoiceQuestion extends SimpleChoiceQuestion {

	private String correctAnswer2;

    public MultipleChoiceQuestion() {}

    public MultipleChoiceQuestion(Long id, String question, CategoryOfQuestion category, List<String> answers, String correctAnswer, String correctAnswer2) {
        super(id, question, category, answers, correctAnswer);
        this.correctAnswer2 = correctAnswer2;
    }

    public String getCorrectAnswer2() {
        return correctAnswer2;
    }

    public void setCorrectAnswer2(String correctAnswer2) {
        this.correctAnswer2 = correctAnswer2;
    }

    @Override
    public TypeOfQuestion getTipoDePregunta() {
        return TypeOfQuestion.MULTIPLE_CHOICE;
    }
}
