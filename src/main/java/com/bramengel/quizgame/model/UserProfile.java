package com.bramengel.quizgame.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String displayName;

    private String avatarPath;

    @Enumerated(EnumType.STRING)
    private Difficulty preferredDifficulty;

    public UserProfile() {}

    public UserProfile(User user, String displayName) {
        this.user = user;
        this.displayName = displayName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

    public Difficulty getPreferredDifficulty() { return preferredDifficulty; }
    public void setPreferredDifficulty(Difficulty preferredDifficulty) { this.preferredDifficulty = preferredDifficulty; }
}
