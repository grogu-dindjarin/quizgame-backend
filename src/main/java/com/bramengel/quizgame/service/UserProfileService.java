package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.UserProfileResponse;
import com.bramengel.quizgame.dto.UserProfileUpdateRequest;
import com.bramengel.quizgame.exception.BadRequestException;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Difficulty;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.model.UserProfile;
import com.bramengel.quizgame.repository.UserProfileRepository;
import com.bramengel.quizgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RecordNotFoundException("Profiel niet gevonden voor gebruiker: " + userId));
        return toResponse(profile);
    }

    public UserProfileResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden: " + email));
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RecordNotFoundException("Profiel niet gevonden voor gebruiker: " + user.getId()));
        return toResponse(profile);
    }

    public UserProfileResponse updateProfile(Long userId, UserProfileUpdateRequest req) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RecordNotFoundException("Profiel niet gevonden voor gebruiker: " + userId));

        profile.setDisplayName(req.getDisplayName());

        if (req.getPreferredDifficulty() != null && !req.getPreferredDifficulty().isBlank()) {
            try {
                profile.setPreferredDifficulty(Difficulty.valueOf(req.getPreferredDifficulty().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Ongeldige moeilijkheidsgraad: " + req.getPreferredDifficulty());
            }
        }

        userProfileRepository.save(profile);
        return toResponse(profile);
    }

    public UserProfileResponse uploadAvatar(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden: " + email));
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RecordNotFoundException("Profiel niet gevonden voor gebruiker: " + user.getId()));

        if (file.isEmpty()) {
            throw new BadRequestException("Bestand mag niet leeg zijn");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String filename = user.getId() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), targetPath);

            profile.setAvatarPath("/" + uploadDir + "/" + filename);
            userProfileRepository.save(profile);
        } catch (IOException e) {
            throw new BadRequestException("Avatar opslaan mislukt: " + e.getMessage());
        }

        return toResponse(profile);
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        String difficulty = profile.getPreferredDifficulty() != null
                ? profile.getPreferredDifficulty().name()
                : null;
        return new UserProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getDisplayName(),
                profile.getAvatarPath(),
                difficulty
        );
    }
}
