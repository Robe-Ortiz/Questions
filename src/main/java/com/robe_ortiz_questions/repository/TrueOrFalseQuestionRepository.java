package com.robe_ortiz_questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.robe_ortiz_questions.entity.TrueOrFalseQuestion;

@Repository
public interface TrueOrFalseQuestionRepository extends JpaRepository<TrueOrFalseQuestion, Long>{

}
