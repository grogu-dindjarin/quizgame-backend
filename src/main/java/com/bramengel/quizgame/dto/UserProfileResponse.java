package com.bramengel.quizgame.dto;

import com.bramengel.quizgame.model.Difficulty;

public class UserProfileResponse {

    private Long id;
    private Long userId;
    private String displayName;
    private String avatarPath;
    private Difficulty preferredDifficulty;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, Long userId, String displayName, String avatarPath, Difficulty preferredDifficulty) {
        this.id = id;
        this.userId = userId;
        this.displayName = displayName;
        this.avatarPath = avatarPath;
        this.preferredDifficulty = preferredDifficulty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

    public Difficulty getPreferredDifficulty() { return preferredDifficulty; }
    public void setPreferredDifficulty(Difficulty preferredDifficulty) { this.preferredDifficulty = preferredDifficulty; }
}
