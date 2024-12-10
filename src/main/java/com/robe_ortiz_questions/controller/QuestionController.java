package com.robe_ortiz_questions.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.robe_ortiz_questions.entity.Question;
import com.robe_ortiz_questions.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/all")
	public String showAllQuestions(Model model) {		
		model.addAttribute("questions",questionService.getAllQuestions());
		return "questions";
	}
	
	
	@GetMapping("{id}")
	public String showQuestionById(@PathVariable long id, Model model) {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("question", question);		
		return "question-info";
	}
	
	@GetMapping("/cargar/{fileName}")
	public String processQuestionsFromTheServerFile(@PathVariable("fileName") String fileName, Model model) {	    
	    try {
	    	questionService.processQuestionsFromTheServerFile("static/data/" + fileName + ".json");
	    }catch (JpaSystemException e) {
			System.err.println("Failure to load server file questions");
			e.printStackTrace();
		}
	    model.addAttribute("questions", questionService.getAllQuestions());
	    return "redirect:/question/all";
	}
	
	@GetMapping("/borrar")
	public String cargar(RedirectAttributes redirectAttributes) {
		questionService.borrarTodo();
		redirectAttributes.addFlashAttribute("message", "All questions have been deleted.");
		 return "redirect:/question/all";
	}
	
	@GetMapping("/new/question-file")
	public String addQuestionFile(Model model) {
		model.addAttribute("lastId", questionService.getIdLastQuestion());
		return "add-question-file";
	}
	
	@PostMapping("/upload")
    public String uploadQuestionFromTheFormFile(@RequestParam MultipartFile file, Model model) {
        try {            
            String jsonContent = new String(file.getBytes());     
            questionService.processQuestionsFromTheFormFile(jsonContent);
            model.addAttribute("questions", questionService.getAllQuestions());
            return "questions";  
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to process the file");
            return "add-question-file";  
        }
    }	
}
