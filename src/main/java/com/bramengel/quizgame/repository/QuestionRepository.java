package com.bramengel.quizgame.repository;

import com.bramengel.quizgame.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findBySubcategoryId(Long subcategoryId);
}
