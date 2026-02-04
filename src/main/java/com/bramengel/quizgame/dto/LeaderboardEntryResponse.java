package com.bramengel.quizgame.dto;

public class LeaderboardEntryResponse {

    private int rank;
    private Long userId;
    private String displayName;
    private String avatarPath;
    private int totalScore;
    private int quizCount;

    public LeaderboardEntryResponse() {
    }

    public LeaderboardEntryResponse(int rank, Long userId, String displayName, String avatarPath, int totalScore, int quizCount) {
        this.rank = rank;
        this.userId = userId;
        this.displayName = displayName;
        this.avatarPath = avatarPath;
        this.totalScore = totalScore;
        this.quizCount = quizCount;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getQuizCount() { return quizCount; }
    public void setQuizCount(int quizCount) { this.quizCount = quizCount; }
}
