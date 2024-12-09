package com.robe_ortiz_questions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/cargar/{fileName}")
	public String processQuestionsFromTheServerFile(@PathVariable("fileName") String fileName, Model model) {	    
	    questionService.processQuestionsFromTheServerFile("static/data/" + fileName + ".json");
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
	public String addQuestionFile() {		
		return "add-question-file";
	}
	
	@PostMapping("/upload")
    public String uploadQuestionFromTheFormFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Convertimos el archivo MultipartFile en una cadena JSON
            String jsonContent = new String(file.getBytes());

            // Llamamos al método processQuestions para procesar el archivo
            questionService.processQuestionsFromTheFormFile(jsonContent);

            // Redirigimos a la vista que muestra todas las preguntas
            model.addAttribute("questions", questionService.getAllQuestions());
            return "questions";  // Asegúrate de que esta vista exista

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to process the file");
            return "add-question-file";  // Vista de carga de archivo en caso de error
        }
    }	
}
