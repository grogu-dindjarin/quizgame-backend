package com.bramengel.quizgame.dto;

import java.util.List;

public class CategoryResponse {

    private Long id;
    private String name;
    private int subcategoryCount;
    private List<SubcategoryResponse> subcategories;

    public CategoryResponse() {}

    public CategoryResponse(Long id, String name, int subcategoryCount, List<SubcategoryResponse> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategoryCount = subcategoryCount;
        this.subcategories = subcategories;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSubcategoryCount() { return subcategoryCount; }
    public void setSubcategoryCount(int subcategoryCount) { this.subcategoryCount = subcategoryCount; }

    public List<SubcategoryResponse> getSubcategories() { return subcategories; }
    public void setSubcategories(List<SubcategoryResponse> subcategories) { this.subcategories = subcategories; }
}
