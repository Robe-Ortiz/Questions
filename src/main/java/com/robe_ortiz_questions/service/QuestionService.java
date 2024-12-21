package com.robe_ortiz_questions.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.Question;
import com.robe_ortiz_questions.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	public void borrarTodo() {
		questionRepository.deleteAll();
	}
		
	public Question getQuestionById(long id) {
		return questionRepository.findById(id).orElse(null);
	}
	
 	public Page<Question> getAllQuestionsPageables(){
 		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id")));
 		return questionRepository.findAll(pageable);
 	}
 	
 	public Page<Question> getAllQuestionsPageables(int page, int size){
 		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
 		return questionRepository.findAll(pageable);
 	}
 	 	
 	public Page<Question> getAllQuestionsPageables(CategoryOfQuestion category, int page, int size) {
 		PageRequest pageable = PageRequest.of(page, size);
 		return questionRepository.findByCategoryOrderByIdAsc(category, pageable);
 	}
 	
 	public List<Integer> getNumbersOfPages(Page<Question> questionPage){
 		return IntStream.range(0, questionPage.getTotalPages())
 				.boxed()
 				.collect(Collectors.toList());
 	}
 	
 	public void saveQuestion(Question question) {
 		questionRepository.save(question);
 	}

	public void processQuestionsFromTheServerFile(String classPath) {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource(classPath);

		try {
			String jsonString = new String(Files.readAllBytes(resource.getFile().toPath()));
			List<Question> questions = objectMapper.readValue(jsonString,
					objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
			for (Question question : questions) {
	        	if (!questionRepository.existsByQuestion(question.getQuestion())) {
	        		questionRepository.save(question);
	        	}
			}
		} catch (IOException e) {
			System.err.println("Failure to load server file questions");
			e.printStackTrace();
		}
	}

	public void processQuestionsFromTheFormFile(String jsonContent) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {	       
	        List<Question> questions = objectMapper.readValue(jsonContent,
	                objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
	       
	        for (Question question : questions) {
	        	if (!questionRepository.existsByQuestion(question.getQuestion())) {
	        		questionRepository.save(question);
	        	}
	        		
	        }
	    } catch (IOException e) {
	       System.err.println("Failure to load form questions");
	       e.printStackTrace();
	    }
	}

	

}
