package com.robe_ortiz_questions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.robe_ortiz_questions.entity.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
	
	 List<Question> findByCategoryOrderByIdAsc(CategoryOfQuestion category);
	 
	 boolean existsByQuestion(String question);
}
