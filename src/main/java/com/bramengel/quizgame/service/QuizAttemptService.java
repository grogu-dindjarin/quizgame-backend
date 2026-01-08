package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.QuizAttemptRequest;
import com.bramengel.quizgame.dto.QuizAttemptResponse;
import com.bramengel.quizgame.dto.QuizAttemptResponse.BadgeResponse;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Answer;
import com.bramengel.quizgame.model.Badge;
import com.bramengel.quizgame.model.QuizAttempt;
import com.bramengel.quizgame.model.Subcategory;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.repository.AnswerRepository;
import com.bramengel.quizgame.repository.QuizAttemptRepository;
import com.bramengel.quizgame.repository.SubcategoryRepository;
import com.bramengel.quizgame.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final BadgeService badgeService;

    public QuizAttemptService(
            QuizAttemptRepository quizAttemptRepository,
            SubcategoryRepository subcategoryRepository,
            UserRepository userRepository,
            AnswerRepository answerRepository,
            BadgeService badgeService) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.badgeService = badgeService;
    }

    @Transactional
    public QuizAttemptResponse submitAttempt(String userEmail, QuizAttemptRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden"));

        Subcategory subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                .orElseThrow(() -> new RecordNotFoundException("Subcategorie niet gevonden"));

        int totalQuestions = request.getAnswers().size();
        int score = calculateScore(request.getAnswers());

        QuizAttempt attempt = new QuizAttempt(user, subcategory, score, totalQuestions);
        QuizAttempt saved = quizAttemptRepository.save(attempt);

        int totalAttempts = quizAttemptRepository.findByUserId(user.getId()).size();

        List<Badge> newBadges = badgeService.checkAndAwardBadges(
                user, score, totalQuestions, totalAttempts, subcategory.getDifficulty());

        return buildResponse(saved, subcategory, newBadges);
    }

    private int calculateScore(List<QuizAttemptRequest.AnswerSubmission> answers) {
        int correctCount = 0;
        for (QuizAttemptRequest.AnswerSubmission submission : answers) {
            if (submission.getAnswerId() == null) {
                continue;
            }
            Optional<Answer> answer = answerRepository.findById(submission.getAnswerId());
            if (answer.isPresent() && answer.get().isCorrect()) {
                correctCount++;
            }
        }
        return correctCount + 1;
    }

    private QuizAttemptResponse buildResponse(QuizAttempt attempt, Subcategory subcategory, List<Badge> earnedBadges) {
        QuizAttemptResponse response = new QuizAttemptResponse();
        response.setId(attempt.getId());
        response.setScore(attempt.getScore());
        response.setTotalQuestions(attempt.getTotalQuestions());
        response.setPercentage(attempt.getTotalQuestions() == 0
                ? 0.0
                : (double) attempt.getScore() / attempt.getTotalQuestions() * 100.0);
        response.setSubcategoryId(subcategory.getId());
        response.setSubcategoryName(subcategory.getName());
        response.setPlayedAt(attempt.getPlayedAt());
        response.setEarnedBadges(earnedBadges.stream()
                .map(b -> new BadgeResponse(b.getId(), b.getName(), b.getDescription(), b.getIconPath()))
                .toList());
        return response;
    }
}
