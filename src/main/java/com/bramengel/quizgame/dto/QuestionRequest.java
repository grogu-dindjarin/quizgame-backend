package com.bramengel.quizgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionRequest {

    @NotBlank
    private String text;

    @NotNull
    private Long subcategoryId;

    private String imagePath;

    public QuestionRequest() {}

    public QuestionRequest(String text, Long subcategoryId, String imagePath) {
        this.text = text;
        this.subcategoryId = subcategoryId;
        this.imagePath = imagePath;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Long getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Long subcategoryId) { this.subcategoryId = subcategoryId; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
