package com.bramengel.quizgame.dto;

public class AnswerResponse {

    private Long id;
    private String text;
    private boolean correct;
    private Long questionId;

    public AnswerResponse() {}

    public AnswerResponse(Long id, String text, boolean correct, Long questionId) {
        this.id = id;
        this.text = text;
        this.correct = correct;
        this.questionId = questionId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
}
