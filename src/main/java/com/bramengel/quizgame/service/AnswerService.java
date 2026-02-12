package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.AnswerRequest;
import com.bramengel.quizgame.dto.AnswerResponse;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Answer;
import com.bramengel.quizgame.model.Question;
import com.bramengel.quizgame.repository.AnswerRepository;
import com.bramengel.quizgame.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> findByQuestionId(Long questionId) {
        findQuestionOrThrow(questionId);
        return answerRepository.findByQuestionId(questionId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnswerResponse findById(Long id) {
        return toResponse(findAnswerOrThrow(id));
    }

    @Transactional
    public AnswerResponse create(Long questionId, AnswerRequest req) {
        Question question = findQuestionOrThrow(questionId);
        Answer answer = new Answer(req.getText(), req.isCorrect(), question);
        return toResponse(answerRepository.save(answer));
    }

    @Transactional
    public AnswerResponse update(Long id, AnswerRequest req) {
        Answer answer = findAnswerOrThrow(id);
        answer.setText(req.getText());
        answer.setCorrect(req.isCorrect());
        return toResponse(answerRepository.save(answer));
    }

    @Transactional
    public void delete(Long id) {
        findAnswerOrThrow(id);
        answerRepository.deleteById(id);
    }

    private Question findQuestionOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Vraag niet gevonden met id: " + id));
    }

    private Answer findAnswerOrThrow(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Antwoord niet gevonden met id: " + id));
    }

    private AnswerResponse toResponse(Answer a) {
        return new AnswerResponse(a.getId(), a.getText(), a.isCorrect(), a.getQuestion().getId());
    }
}
