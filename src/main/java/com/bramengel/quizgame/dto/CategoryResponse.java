package com.bramengel.quizgame.dto;

public class CategoryResponse {

    private Long id;
    private String name;
    private int subcategoryCount;

    public CategoryResponse() {}

    public CategoryResponse(Long id, String name, int subcategoryCount) {
        this.id = id;
        this.name = name;
        this.subcategoryCount = subcategoryCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSubcategoryCount() { return subcategoryCount; }
    public void setSubcategoryCount(int subcategoryCount) { this.subcategoryCount = subcategoryCount; }
}
