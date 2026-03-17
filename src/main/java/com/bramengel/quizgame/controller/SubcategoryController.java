package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.SubcategoryRequest;
import com.bramengel.quizgame.dto.SubcategoryResponse;
import com.bramengel.quizgame.dto.SubcategoryUpdateRequest;
import com.bramengel.quizgame.service.SubcategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping
    public ResponseEntity<List<SubcategoryResponse>> findAll() {
        return ResponseEntity.ok(subcategoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(subcategoryService.findById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubcategoryResponse>> findByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subcategoryService.findByCategoryId(categoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubcategoryResponse> create(@Valid @RequestBody SubcategoryRequest req) {
        return ResponseEntity.status(201).body(subcategoryService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubcategoryResponse> update(@PathVariable Long id, @Valid @RequestBody SubcategoryUpdateRequest req) {
        return ResponseEntity.ok(subcategoryService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subcategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
