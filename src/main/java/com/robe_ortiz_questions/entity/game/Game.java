package com.robe_ortiz_questions.entity.game;

import java.util.List;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;

public class Game {
    private int numberOfQuestions;
    private CategoryOfQuestion categoryOfQuestions;
    private List<Question> listOfQuestions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;

    public Game(int numberOfQuestions, CategoryOfQuestion categoryOfQuestions, List<Question> listOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
        this.categoryOfQuestions = categoryOfQuestions;
        this.listOfQuestions = listOfQuestions;
    }

    public void incrementCurrentQuestionIndex() {
        this.currentQuestionIndex++;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
    }

    public void incrementWrongAnswers() {
        this.wrongAnswers++;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
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

