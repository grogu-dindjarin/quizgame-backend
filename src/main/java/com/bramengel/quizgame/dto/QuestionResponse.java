package com.bramengel.quizgame.dto;

public class QuestionResponse {

    private Long id;
    private String text;
    private String imagePath;
    private Long subcategoryId;
    private String subcategoryName;

    public QuestionResponse() {}

    public QuestionResponse(Long id, String text, String imagePath, Long subcategoryId, String subcategoryName) {
        this.id = id;
        this.text = text;
        this.imagePath = imagePath;
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Long getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Long subcategoryId) { this.subcategoryId = subcategoryId; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }
}
