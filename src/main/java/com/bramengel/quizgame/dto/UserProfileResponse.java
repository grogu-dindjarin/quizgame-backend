package com.bramengel.quizgame.dto;

public class UserProfileResponse {

    private Long id;
    private Long userId;
    private String displayName;
    private String avatarPath;
    private String preferredDifficulty;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, Long userId, String displayName, String avatarPath, String preferredDifficulty) {
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

    public String getPreferredDifficulty() { return preferredDifficulty; }
    public void setPreferredDifficulty(String preferredDifficulty) { this.preferredDifficulty = preferredDifficulty; }
}
