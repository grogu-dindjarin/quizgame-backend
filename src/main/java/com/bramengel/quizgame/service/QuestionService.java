package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.QuestionRequest;
import com.bramengel.quizgame.dto.QuestionResponse;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Question;
import com.bramengel.quizgame.model.Subcategory;
import com.bramengel.quizgame.repository.QuestionRepository;
import com.bramengel.quizgame.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubcategoryRepository subcategoryRepository;

    public QuestionService(QuestionRepository questionRepository, SubcategoryRepository subcategoryRepository) {
        this.questionRepository = questionRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findAll() {
        return questionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse findById(Long id) {
        return toResponse(findQuestionOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findBySubcategoryId(Long subcategoryId) {
        findSubcategoryOrThrow(subcategoryId);
        return questionRepository.findBySubcategoryId(subcategoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public QuestionResponse create(QuestionRequest req) {
        Subcategory subcategory = findSubcategoryOrThrow(req.getSubcategoryId());
        Question question = new Question(req.getText(), subcategory);
        question.setImagePath(req.getImagePath());
        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public QuestionResponse update(Long id, QuestionRequest req) {
        Question question = findQuestionOrThrow(id);
        Subcategory subcategory = findSubcategoryOrThrow(req.getSubcategoryId());
        question.setText(req.getText());
        question.setImagePath(req.getImagePath());
        question.setSubcategory(subcategory);
        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public void delete(Long id) {
        findQuestionOrThrow(id);
        questionRepository.deleteById(id);
    }

    private Subcategory findSubcategoryOrThrow(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Subcategory not found with id: " + id));
    }

    private Question findQuestionOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Question not found with id: " + id));
    }

    private QuestionResponse toResponse(Question q) {
        return new QuestionResponse(
                q.getId(),
                q.getText(),
                q.getImagePath(),
                q.getSubcategory().getId(),
                q.getSubcategory().getName()
        );
    }
}
