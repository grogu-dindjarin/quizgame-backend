package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.QuizAttemptRequest;
import com.bramengel.quizgame.dto.QuizAttemptResponse;
import com.bramengel.quizgame.service.QuizAttemptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping
    public ResponseEntity<QuizAttemptResponse> submitAttempt(
            @Valid @RequestBody QuizAttemptRequest request,
            Principal principal) {
        QuizAttemptResponse response = quizAttemptService.submitAttempt(principal.getName(), request);
        return ResponseEntity.ok(response);
    }
}
