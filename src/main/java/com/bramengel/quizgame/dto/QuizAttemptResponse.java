package com.bramengel.quizgame.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizAttemptResponse {

    private Long id;
    private int score;
    private int totalQuestions;
    private double percentage;
    private List<BadgeResponse> earnedBadges;
    private Long subcategoryId;
    private String subcategoryName;
    private LocalDateTime playedAt;

    public QuizAttemptResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public List<BadgeResponse> getEarnedBadges() { return earnedBadges; }
    public void setEarnedBadges(List<BadgeResponse> earnedBadges) { this.earnedBadges = earnedBadges; }

    public Long getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Long subcategoryId) { this.subcategoryId = subcategoryId; }

    public String getSubcategoryName() { return subcategoryName; }
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public LocalDateTime getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDateTime playedAt) { this.playedAt = playedAt; }

    public static class BadgeResponse {

        private Long id;
        private String name;
        private String description;
        private String iconPath;

        public BadgeResponse() {}

        public BadgeResponse(Long id, String name, String description, String iconPath) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.iconPath = iconPath;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getIconPath() { return iconPath; }
        public void setIconPath(String iconPath) { this.iconPath = iconPath; }
    }
}
