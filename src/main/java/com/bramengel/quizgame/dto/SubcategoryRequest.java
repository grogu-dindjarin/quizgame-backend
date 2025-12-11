package com.bramengel.quizgame.dto;

import com.bramengel.quizgame.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubcategoryRequest {

    @NotBlank
    private String name;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Long categoryId;

    public SubcategoryRequest() {}

    public SubcategoryRequest(String name, Difficulty difficulty, Long categoryId) {
        this.name = name;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
