package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.UserProfileResponse;
import com.bramengel.quizgame.dto.UserProfileUpdateRequest;
import com.bramengel.quizgame.repository.UserRepository;
import com.bramengel.quizgame.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    public UserProfileController(UserProfileService userProfileService, UserRepository userRepository) {
        this.userProfileService = userProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getMyProfile(Principal principal) {
        return ResponseEntity.ok(userProfileService.getProfileByEmail(principal.getName()));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            Principal principal,
            @Valid @RequestBody UserProfileUpdateRequest req
    ) {
        Long userId = userRepository.findByEmail(principal.getName())
                .orElseThrow().getId();
        return ResponseEntity.ok(userProfileService.updateProfile(userId, req));
    }

    @PostMapping("/avatar")
    public ResponseEntity<UserProfileResponse> uploadAvatar(
            Principal principal,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(userProfileService.uploadAvatar(principal.getName(), file));
    }
}
