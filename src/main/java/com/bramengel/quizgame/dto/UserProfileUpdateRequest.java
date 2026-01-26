package com.bramengel.quizgame.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest {

    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;

    private String preferredDifficulty;

    public UserProfileUpdateRequest() {
    }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPreferredDifficulty() { return preferredDifficulty; }
    public void setPreferredDifficulty(String preferredDifficulty) { this.preferredDifficulty = preferredDifficulty; }
}
