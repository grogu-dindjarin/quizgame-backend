package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.LeaderboardEntryResponse;
import com.bramengel.quizgame.model.QuizAttempt;
import com.bramengel.quizgame.model.UserProfile;
import com.bramengel.quizgame.repository.QuizAttemptRepository;
import com.bramengel.quizgame.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LeaderboardService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserProfileRepository userProfileRepository;

    public LeaderboardService(QuizAttemptRepository quizAttemptRepository, UserProfileRepository userProfileRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard() {
        List<QuizAttempt> attempts = quizAttemptRepository.findAll();

        Map<Long, int[]> aggregates = new HashMap<>();
        for (QuizAttempt attempt : attempts) {
            Long userId = attempt.getUser().getId();
            aggregates.computeIfAbsent(userId, k -> new int[]{0, 0});
            aggregates.get(userId)[0] += attempt.getScore();
            aggregates.get(userId)[1] += 1;
        }

        List<Map.Entry<Long, int[]>> sorted = new ArrayList<>(aggregates.entrySet());
        sorted.sort(Comparator.comparingInt((Map.Entry<Long, int[]> e) -> e.getValue()[0]).reversed());

        List<LeaderboardEntryResponse> leaderboard = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<Long, int[]> entry : sorted) {
            Long userId = entry.getKey();
            int totalScore = entry.getValue()[0];
            int quizCount = entry.getValue()[1];

            Optional<UserProfile> profileOpt = userProfileRepository.findByUserId(userId);
            String displayName = profileOpt.map(UserProfile::getDisplayName).orElse("Onbekend");
            String avatarPath = profileOpt.map(UserProfile::getAvatarPath).orElse(null);

            leaderboard.add(new LeaderboardEntryResponse(rank, userId, displayName, avatarPath, totalScore, quizCount));
            rank++;
        }

        return leaderboard;
    }
}
