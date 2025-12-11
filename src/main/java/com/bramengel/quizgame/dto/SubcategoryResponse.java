package com.bramengel.quizgame.dto;

import com.bramengel.quizgame.model.Difficulty;

public class SubcategoryResponse {

    private Long id;
    private String name;
    private Difficulty difficulty;
    private Long categoryId;
    private String categoryName;
    private int questionCount;

    public SubcategoryResponse() {}

    public SubcategoryResponse(Long id, String name, Difficulty difficulty, Long categoryId, String categoryName, int questionCount) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.questionCount = questionCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
}
