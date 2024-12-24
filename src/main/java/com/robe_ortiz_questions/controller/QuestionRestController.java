package com.robe_ortiz_questions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;
import com.robe_ortiz_questions.service.QuestionService;

@RestController
@RequestMapping("/api/question")
public class QuestionRestController {

	@Autowired
	private QuestionService questionService;
	
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.findAllQuestions();
        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(questions);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(question);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        questionService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Question> updateQuestionById(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        Question question = questionService.getQuestionById(id);

        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
       
        if (question instanceof MultipleQuestion && updatedQuestion instanceof MultipleQuestion) {
            MultipleQuestion multipleQuestion = (MultipleQuestion) question;
            MultipleQuestion updatedMultipleQuestion = (MultipleQuestion) updatedQuestion;

            multipleQuestion.setQuestion(updatedMultipleQuestion.getQuestion());
            multipleQuestion.setCorrectAnswers(updatedMultipleQuestion.getCorrectAnswers());
            multipleQuestion.setIncorrectAnswers(updatedMultipleQuestion.getIncorrectAnswers());
            
        }else if (question instanceof TrueOrFalseQuestion && updatedQuestion instanceof TrueOrFalseQuestion) {
            TrueOrFalseQuestion trueOrFalseQuestion = (TrueOrFalseQuestion) question;
            TrueOrFalseQuestion updatedTrueOrFalseQuestion = (TrueOrFalseQuestion) updatedQuestion;

            trueOrFalseQuestion.setQuestion(updatedTrueOrFalseQuestion.getQuestion());
            trueOrFalseQuestion.setAnswer(updatedTrueOrFalseQuestion.getAnswer());
        }

        Question savedQuestion = questionService.save(question);
        return ResponseEntity.ok(savedQuestion);
    }
}
