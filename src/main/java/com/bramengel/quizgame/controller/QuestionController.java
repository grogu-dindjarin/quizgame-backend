package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.QuestionRequest;
import com.bramengel.quizgame.dto.QuestionResponse;
import com.bramengel.quizgame.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> findAll() {
        return ResponseEntity.ok(questionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<QuestionResponse>> findBySubcategoryId(@PathVariable Long subcategoryId) {
        return ResponseEntity.ok(questionService.findBySubcategoryId(subcategoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> create(@Valid @RequestBody QuestionRequest req) {
        return ResponseEntity.status(201).body(questionService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> update(@PathVariable Long id, @Valid @RequestBody QuestionRequest req) {
        return ResponseEntity.ok(questionService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
