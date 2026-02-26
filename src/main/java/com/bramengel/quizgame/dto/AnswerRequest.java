package com.bramengel.quizgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnswerRequest {

    @NotBlank(message = "Antwoordtekst is verplicht")
    @Size(min = 1, max = 255, message = "Antwoordtekst mag maximaal 255 tekens bevatten")
    private String text;

    private boolean correct;

    public AnswerRequest() {}

    public AnswerRequest(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
