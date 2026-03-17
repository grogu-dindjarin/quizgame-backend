package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.SubcategoryRequest;
import com.bramengel.quizgame.dto.SubcategoryResponse;
import com.bramengel.quizgame.dto.SubcategoryUpdateRequest;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Category;
import com.bramengel.quizgame.model.Subcategory;
import com.bramengel.quizgame.repository.CategoryRepository;
import com.bramengel.quizgame.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<SubcategoryResponse> findAll() {
        return subcategoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubcategoryResponse findById(Long id) {
        return toResponse(findSubcategoryOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<SubcategoryResponse> findByCategoryId(Long categoryId) {
        findCategoryOrThrow(categoryId);
        return subcategoryRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SubcategoryResponse create(SubcategoryRequest req) {
        Category category = findCategoryOrThrow(req.getCategoryId());
        Subcategory subcategory = new Subcategory(req.getName(), req.getDifficulty(), category);
        return toResponse(subcategoryRepository.save(subcategory));
    }

    @Transactional
    public SubcategoryResponse update(Long id, SubcategoryUpdateRequest req) {
        Subcategory subcategory = findSubcategoryOrThrow(id);
        subcategory.setName(req.getName());
        subcategory.setDifficulty(req.getDifficulty());
        return toResponse(subcategoryRepository.save(subcategory));
    }

    @Transactional
    public void delete(Long id) {
        findSubcategoryOrThrow(id);
        subcategoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Categorie niet gevonden met id: " + id));
    }

    private Subcategory findSubcategoryOrThrow(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Subcategorie niet gevonden met id: " + id));
    }

    private SubcategoryResponse toResponse(Subcategory s) {
        return new SubcategoryResponse(
                s.getId(),
                s.getName(),
                s.getDifficulty(),
                s.getCategory().getId(),
                s.getCategory().getName(),
                s.getQuestions().size()
        );
    }
}
