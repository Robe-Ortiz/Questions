package com.robe_ortiz_questions.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robe_ortiz_questions.entity.Question;
import com.robe_ortiz_questions.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	public void borrarTodo() {
		questionRepository.deleteAll();
	}
	
	public long getIdLastQuestion() {
		Question lastQuestion = questionRepository.findFirstByOrderByIdDesc();
		return lastQuestion == null ? 0 : lastQuestion.getId();
	}
	
	public Question getQuestionById(long id) {
		return questionRepository.findById(id).orElse(null);
	}
	
 	public List<Question> getAllQuestions() {
		return questionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public void processQuestionsFromTheServerFile(String classPath) {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource(classPath);

		try {
			String jsonString = new String(Files.readAllBytes(resource.getFile().toPath()));
			List<Question> questions = objectMapper.readValue(jsonString,
					objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
			for (Question question : questions) {
				questionRepository.save(question);
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
	            questionRepository.save(question);
	        }
	    } catch (IOException e) {
	       System.err.println("Failure to load form questions");
	       e.printStackTrace();
	    }
	}

	

}