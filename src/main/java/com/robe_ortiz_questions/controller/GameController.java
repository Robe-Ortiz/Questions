package com.robe_ortiz_questions.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.service.GameService;

@Controller
@RequestMapping("/game")
@SessionAttributes({"questions", "currentQuestionIndex", "correctAnswers", "wrongAnswers"})
public class GameController {
	
	private static Random random = new Random();
	
	@Autowired
	private GameService gameService;
	


	@GetMapping("/category")
	public String selectCategory(Model model) {
		model.addAttribute("categories", CategoryOfQuestion.values());
		model.addAttribute("showCategoryForm", true);
		return"game-options";
	}
	
	@GetMapping("/number-of-questions")
	public String selectNumberOfQuestions(@RequestParam("category") String category, Model model) {
		model.addAttribute("showNumberOfQuestionsForm", true);
		model.addAttribute("category", category);
		return "game-options";
	}
	
	@PostMapping("/answer")
	public String answer(@RequestParam("answer") List<String> answers,  // Recibir todas las respuestas seleccionadas
	                     @ModelAttribute("questions") List<Question> questions,
	                     @ModelAttribute("currentQuestionIndex") int currentQuestionIndex,
	                     @ModelAttribute("correctAnswers") int correctAnswers,
	                     @ModelAttribute("wrongAnswers") int wrongAnswers,
	                     Model model) {
	    Question currentQuestion = questions.get(currentQuestionIndex);

	    if (currentQuestion instanceof TrueOrFalseQuestion trueOrFalseQuestion) {
	        if (Boolean.parseBoolean(answers.get(0)) == trueOrFalseQuestion.getAnswer()) {
	            correctAnswers++;
	        } else {
	            wrongAnswers++;
	        }
	    } else if (currentQuestion instanceof MultipleQuestion multipleQuestion) {
	        List<String> correctAnswersList = multipleQuestion.getCorrectAnswers();
	        
	        if (correctAnswersList.containsAll(answers)) {
	            correctAnswers++;
	        } else {
	            wrongAnswers++;
	        }
	    }

	    currentQuestionIndex++;
	    model.addAttribute("currentQuestionIndex", currentQuestionIndex);
	    model.addAttribute("correctAnswers", correctAnswers);
	    model.addAttribute("wrongAnswers", wrongAnswers);

	    if (currentQuestionIndex < questions.size()) {
	        return showQuestion(model);
	    } else {
	        return "redirect:/game/result";
	    }
	}

    private String showQuestion(Model model) {
        List<Question> questions = (List<Question>) model.getAttribute("questions");
        int currentQuestionIndex = (int) model.getAttribute("currentQuestionIndex");

        Question question = questions.get(currentQuestionIndex);
        model.addAttribute("question", question);

        if (question instanceof MultipleQuestion multipleQuestion) {
        	boolean showMultipleCorrectAnswers = false;

        	List<String> allAnswers = new ArrayList<>();      	           	    
    	    List<String> shuffledCorrectAnswers = new ArrayList<>(multipleQuestion.getCorrectAnswers());
    	    List<String> shuffledIncorrectAnswers = new ArrayList<>(multipleQuestion.getIncorrectAnswers());
    	    
    	    Collections.shuffle(shuffledCorrectAnswers);
    	    Collections.shuffle(shuffledIncorrectAnswers);
    	    
    	    if(shuffledCorrectAnswers.size() > 1) {
    	    	showMultipleCorrectAnswers = random.nextBoolean();
    	    }
    	    	
            if (showMultipleCorrectAnswers) {
                allAnswers.addAll(shuffledCorrectAnswers.subList(0, 2));
                allAnswers.addAll(shuffledIncorrectAnswers.subList(0, 2)); 
            } else {
                allAnswers.add(shuffledCorrectAnswers.get(0));
                allAnswers.addAll(shuffledIncorrectAnswers.subList(0, 3)); 
            }
              	    
    	    
    	    Collections.shuffle(allAnswers);
    	    model.addAttribute("answers", allAnswers);
    	    model.addAttribute("showMultipleCorrectAnswers", showMultipleCorrectAnswers);
        }

        return "game";
    }
          

    @GetMapping("/result")
    public String showResults(Model model) {
    	model.addAttribute("showResult",true);
        model.addAttribute("correctAnswers", model.getAttribute("correctAnswers"));
        model.addAttribute("wrongAnswers", model.getAttribute("wrongAnswers"));
        return "game-result";
    }
	
	@GetMapping("/play")
	public String play(@RequestParam("category") String category, @RequestParam("numberOfQuestions") String numberOfQuestions, Model model) { 	
		
		try {		
			List<Question> questions = gameService.getQuestions(Integer.parseInt(numberOfQuestions), CategoryOfQuestion.valueOf(category));
			
			if(questions.size() < Integer.parseInt(numberOfQuestions)) throw new IllegalArgumentException();
            model.addAttribute("questions", questions);
            model.addAttribute("currentQuestionIndex", 0);
            model.addAttribute("correctAnswers", 0);
            model.addAttribute("wrongAnswers", 0);			
            return showQuestion(model);
            
		}catch (IllegalArgumentException e) {			
            model.addAttribute("error", "Actualmente no podemos servir la configuración solicitada.");
            return "game";           
		}
	}
}
