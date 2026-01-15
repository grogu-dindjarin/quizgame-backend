package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.CategoryRequest;
import com.bramengel.quizgame.dto.CategoryResponse;
import com.bramengel.quizgame.dto.SubcategoryResponse;
import com.bramengel.quizgame.exception.BadRequestException;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Category;
import com.bramengel.quizgame.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return toResponse(findCategoryOrThrow(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        if (categoryRepository.existsByName(req.getName())) {
            throw new BadRequestException("Categorie met naam '" + req.getName() + "' bestaat al");
        }
        Category category = new Category(req.getName());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = findCategoryOrThrow(id);
        category.setName(req.getName());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        findCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Categorie niet gevonden met id: " + id));
    }

    private CategoryResponse toResponse(Category c) {
        List<SubcategoryResponse> subs = c.getSubcategories().stream()
                .map(s -> new SubcategoryResponse(s.getId(), s.getName(), s.getDifficulty(),
                        c.getId(), c.getName(), s.getQuestions().size()))
                .toList();
        return new CategoryResponse(c.getId(), c.getName(), subs.size(), subs);
    }
}
