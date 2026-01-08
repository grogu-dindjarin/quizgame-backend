package com.bramengel.quizgame.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class QuizAttemptRequest {

    @NotNull
    private Long subcategoryId;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<AnswerSubmission> answers;

    public QuizAttemptRequest() {}

    public Long getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Long subcategoryId) { this.subcategoryId = subcategoryId; }

    public List<AnswerSubmission> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }

    public static class AnswerSubmission {

        private Long questionId;
        private Long answerId;

        public AnswerSubmission() {}

        public AnswerSubmission(Long questionId, Long answerId) {
            this.questionId = questionId;
            this.answerId = answerId;
        }

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public Long getAnswerId() { return answerId; }
        public void setAnswerId(Long answerId) { this.answerId = answerId; }
    }
}
