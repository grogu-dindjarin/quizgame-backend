package com.bramengel.quizgame.dto;

import com.bramengel.quizgame.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubcategoryUpdateRequest {

    @NotBlank(message = "Naam mag niet leeg zijn")
    private String name;

    @NotNull(message = "Moeilijkheidsgraad is verplicht")
    private Difficulty difficulty;

    public SubcategoryUpdateRequest() {}

    public SubcategoryUpdateRequest(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
}
