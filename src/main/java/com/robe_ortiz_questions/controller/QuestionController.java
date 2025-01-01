package com.robe_ortiz_questions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.MultipleQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.entity.question.QuestionFactory;
import com.robe_ortiz_questions.entity.question.TrueOrFalseQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;
import com.robe_ortiz_questions.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/all")
	public String showAllQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
								   @RequestParam(required = false) CategoryOfQuestion category,Model model) {
	    Page<Question> questionPage;
	    if (category != null) {
	        questionPage = questionService.getAllQuestionsPageables(category, page, size);
	        model.addAttribute("category", category.toString());
	    } else {
	        questionPage = questionService.getAllQuestionsPageables(page, size);
	        model.addAttribute("category", "");
	    }
		model.addAttribute("questions", questionPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", questionPage.getTotalPages());
		model.addAttribute("pageNumbers", questionService.getNumbersOfPages(questionPage));		
		return "questions";
	}

	@GetMapping("/new")
	public String addQuestion(Model model, @RequestParam(required = false) String stage,
			@RequestParam(required = false) String questionType, @RequestParam(required = false) String category) {

		String currentStage = (stage == null) ? "first" : stage;
		model.addAttribute("stage", currentStage);
		model.addAttribute("categories", CategoryOfQuestion.values());
		
		if ("second".equals(currentStage)) {
			model.addAttribute("questionType", questionType);
			model.addAttribute("category", category);
		}
		return "question-add";
	}

	@PostMapping("/new")
	public String processFormToAddNewQuestion(@RequestParam String questionType, @RequestParam String category,
			RedirectAttributes redirectAttributes) {
		if (!"MULTIPLE_QUESTION".equals(questionType) && !"TRUE_OR_FALSE".equals(questionType)) {
			return "redirect:/question/new";
		}
		redirectAttributes.addAttribute("stage", "second");
		redirectAttributes.addAttribute("questionType", questionType);
		redirectAttributes.addAttribute("category", category);
		return "redirect:/question/new";
	}

	@PostMapping("/save")
	public String saveOrUpdateQuestion(@RequestParam(required = false) Long id, 
	                                   @RequestParam String question, 
	                                   @RequestParam(required = false) String correctAnswers,
	                                   @RequestParam(required = false) String incorrectAnswers, 
	                                   @RequestParam(required = false) String answer,
	                                   @RequestParam String questionType, 
	                                   @RequestParam String category, 
	                                   RedirectAttributes redirectAttributes) {
	    try {
	        TypeOfQuestion typeOfQuestion = TypeOfQuestion.valueOf(questionType);
	        CategoryOfQuestion categoryOfQuestion = CategoryOfQuestion.valueOf(category);

	        Object[] extraParams;
	        if (typeOfQuestion == TypeOfQuestion.MULTIPLE_QUESTION) {	            
	            List<String> incorrectAnswersList = List.of(incorrectAnswers.split(","));	            
	            if (incorrectAnswersList.size() < 3)  throw new IllegalArgumentException("Debe haber al menos 3 respuestas incorrectas");            
	            extraParams = new Object[] { incorrectAnswersList, List.of(correctAnswers.split(",")) };
	        } else if (typeOfQuestion == TypeOfQuestion.TRUE_OR_FALSE) {
	            extraParams = new Object[] { Boolean.parseBoolean(answer) };
	        } else {
	            throw new IllegalArgumentException("Tipo de pregunta no válido");
	        }

	        Question questionEntity;

	        if (id != null) {
	            questionEntity = questionService.getQuestionById(id);
	            if (questionEntity == null) {
	                throw new IllegalArgumentException("Pregunta no encontrada con ID: " + id);
	            }
	            questionEntity.setQuestion(question);
	            questionEntity.setCategory(categoryOfQuestion);

	            if (typeOfQuestion == TypeOfQuestion.MULTIPLE_QUESTION && questionEntity instanceof MultipleQuestion) {
	                ((MultipleQuestion) questionEntity).setCorrectAnswers((List<String>) extraParams[1]);
	                ((MultipleQuestion) questionEntity).setIncorrectAnswers((List<String>) extraParams[0]);
	            } else if (typeOfQuestion == TypeOfQuestion.TRUE_OR_FALSE && questionEntity instanceof TrueOrFalseQuestion) {
	                ((TrueOrFalseQuestion) questionEntity).setAnswer((Boolean) extraParams[0]);
	            }
	        } else {	            
	            questionEntity = QuestionFactory.createQuestion(typeOfQuestion, categoryOfQuestion, question, extraParams);
	        }

	        questionService.saveQuestion(questionEntity);
	        redirectAttributes.addFlashAttribute("success", "Pregunta " + (id != null ? "actualizada" : "guardada") + " con éxito");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error al guardar la pregunta. " + e.getMessage());
	    }
	    return "redirect:/question/all";
	}


	
	@GetMapping("/edit/{id}")
	public String editQuestion(@PathVariable Long id, Model model) {
	    Question question = questionService.getQuestionById(id);
	    if (question == null) {
	        throw new IllegalArgumentException("Pregunta no encontrada con ID: " + id);
	    }

	    model.addAttribute("question", question);
	    model.addAttribute("questionText", question.getQuestion());
	    model.addAttribute("categories", CategoryOfQuestion.values());

	    if (question instanceof TrueOrFalseQuestion) {
	        model.addAttribute("isTrueOrFalse", true);
	        model.addAttribute("questionType", TypeOfQuestion.TRUE_OR_FALSE);
	        model.addAttribute("answer", ((TrueOrFalseQuestion) question).getAnswer());
	    } else if (question instanceof MultipleQuestion) {
	        model.addAttribute("isMultipleChoice", true);
	        model.addAttribute("questionType", TypeOfQuestion.MULTIPLE_QUESTION);
	        model.addAttribute("correctAnswers", ((MultipleQuestion) question).getCorrectAnswers());
	        model.addAttribute("incorrectAnswers", ((MultipleQuestion) question).getIncorrectAnswers());
	    }

	    return "question-edit";
	}

    


	@GetMapping("/delete/all")
	public String deleteAll(RedirectAttributes redirectAttributes) {
		questionService.deleteAll();
		redirectAttributes.addFlashAttribute("success", "All questions have been deleted.");
		return "redirect:/question/all";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	    questionService.deleteById(id);
	    redirectAttributes.addFlashAttribute("success", "The question has been deleted");
	    return "redirect:/question/all";
	}

	@GetMapping("/id/{id}")
	public String showQuestionById(@PathVariable long id, Model model) {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("question", question);
		model.addAttribute("activateBackButton", true);
		return "question-info";
	}
	
	

	@GetMapping("/load/{fileName}")
	public String processQuestionsFromTheServerFile(@PathVariable("fileName") String fileName, Model model) {
		try {
			questionService.processQuestionsFromTheServerFile("static/data/" + fileName + ".json");
		} catch (JpaSystemException e) {
			System.err.println("Failure to load server file questions");
			e.printStackTrace();
		}
		model.addAttribute("questions", questionService.getAllQuestionsPageables());
		return "redirect:/question/all";
	}

	@GetMapping("/new/question-file")
	public String addQuestionFile(Model model) {
		return "add-question-file";
	}

	@PostMapping("/upload")
	public String uploadQuestionFromTheFormFile(@RequestParam MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		try {
			String jsonContent = new String(file.getBytes());
			
			 ObjectMapper objectMapper = new ObjectMapper();
		        objectMapper.readTree(jsonContent);
		        
			questionService.processQuestionsFromTheFormFile(jsonContent);
			
			model.addAttribute("questions", questionService.getAllQuestionsPageables());
			redirectAttributes.addFlashAttribute("success", "Todo bien");
			
			return "redirect:/question/all";
		} catch (Exception e) {
			model.addAttribute("message", "Failed to process the file");
			return "add-question-file";
		}
	}
}
