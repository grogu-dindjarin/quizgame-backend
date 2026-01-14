package com.bramengel.quizgame.service;

import com.bramengel.quizgame.model.Badge;
import com.bramengel.quizgame.model.Difficulty;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.model.UserBadge;
import com.bramengel.quizgame.repository.BadgeRepository;
import com.bramengel.quizgame.repository.UserBadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public BadgeService(BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository) {
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    @Transactional
    public List<Badge> checkAndAwardBadges(User user, int score, int totalQuestions, int totalAttempts, Difficulty difficulty) {
        List<Badge> awarded = new ArrayList<>();

        tryAward(user, "FIRST_QUIZ").ifPresent(awarded::add);

        if (score == totalQuestions) {
            tryAward(user, "PERFECT_SCORE").ifPresent(awarded::add);
        }

        if (totalAttempts >= 10) {
            tryAward(user, "TEN_QUIZZES").ifPresent(awarded::add);
        }

        if (difficulty == Difficulty.EXPERT && score > 0) {
            tryAward(user, "EXPERT_QUIZ").ifPresent(awarded::add);
        }

        if (difficulty == Difficulty.BEGINNER && score == totalQuestions) {
            tryAward(user, "BEGINNER_PERFECT").ifPresent(awarded::add);
        }

        return awarded;
    }

    private Optional<Badge> tryAward(User user, String condition) {
        Optional<Badge> badge = badgeRepository.findByCondition(condition);
        if (badge.isEmpty()) {
            return Optional.empty();
        }

        Badge found = badge.get();
        if (userBadgeRepository.existsByUserIdAndBadgeId(user.getId(), found.getId())) {
            return Optional.empty();
        }

        userBadgeRepository.save(new UserBadge(user, found));
        return Optional.of(found);
    }
}
