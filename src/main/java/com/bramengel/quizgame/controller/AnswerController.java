package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.AnswerRequest;
import com.bramengel.quizgame.dto.AnswerResponse;
import com.bramengel.quizgame.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> findByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(answerService.findByQuestionId(questionId));
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> findById(@PathVariable Long questionId, @PathVariable Long answerId) {
        return ResponseEntity.ok(answerService.findById(answerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnswerResponse> create(@PathVariable Long questionId, @Valid @RequestBody AnswerRequest req) {
        AnswerResponse response = answerService.create(questionId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{answerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnswerResponse> update(@PathVariable Long questionId, @PathVariable Long answerId, @Valid @RequestBody AnswerRequest req) {
        return ResponseEntity.ok(answerService.update(answerId, req));
    }

    @DeleteMapping("/{answerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long questionId, @PathVariable Long answerId) {
        answerService.delete(answerId);
        return ResponseEntity.noContent().build();
    }
}
