package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.QuizAttemptRequest;
import com.bramengel.quizgame.dto.QuizAttemptResponse;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Answer;
import com.bramengel.quizgame.model.Badge;
import com.bramengel.quizgame.model.Difficulty;
import com.bramengel.quizgame.model.QuizAttempt;
import com.bramengel.quizgame.model.Role;
import com.bramengel.quizgame.model.Subcategory;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.repository.AnswerRepository;
import com.bramengel.quizgame.repository.QuizAttemptRepository;
import com.bramengel.quizgame.repository.SubcategoryRepository;
import com.bramengel.quizgame.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizAttemptServiceTest {

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private BadgeService badgeService;

    @InjectMocks
    private QuizAttemptService quizAttemptService;

    // --- helpers ---

    private User buildUser() {
        User user = new User("bram@example.com", "hashed", Role.USER);
        user.setId(1L);
        return user;
    }

    private Subcategory buildSubcategory() {
        Subcategory sub = new Subcategory("Geschiedenis", Difficulty.BEGINNER, null);
        sub.setId(10L);
        return sub;
    }

    private Answer correctAnswer(Long id) {
        Answer a = new Answer("Juist antwoord", true, null);
        a.setId(id);
        return a;
    }

    private Answer wrongAnswer(Long id) {
        Answer a = new Answer("Fout antwoord", false, null);
        a.setId(id);
        return a;
    }

    // --- tests ---

    @Test
    void submitAttempt_withValidRequest_returnsResponse() {
        // Arrange
        User user = buildUser();
        Subcategory subcategory = buildSubcategory();

        QuizAttemptRequest.AnswerSubmission submission = new QuizAttemptRequest.AnswerSubmission(1L, 100L);
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategory.getId());
        request.setAnswers(List.of(submission));

        Answer answer = correctAnswer(100L);

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory));
        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(inv -> {
            QuizAttempt saved = inv.getArgument(0);
            saved.setId(99L);
            return saved;
        });
        when(quizAttemptRepository.findByUserId(1L)).thenReturn(List.of(new QuizAttempt()));
        when(badgeService.checkAndAwardBadges(any(), anyInt(), anyInt(), anyInt(), any())).thenReturn(List.of());

        // Act
        QuizAttemptResponse response = quizAttemptService.submitAttempt("bram@example.com", request);

        // Assert
        assertNotNull(response);
        assertEquals(99L, response.getId());
        assertEquals(1, response.getScore());
        assertEquals(1, response.getTotalQuestions());
        assertTrue(response.getEarnedBadges().isEmpty());
    }

    @Test
    void submitAttempt_withUnknownUser_throwsRecordNotFoundException() {
        // Arrange
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(10L);
        request.setAnswers(List.of(new QuizAttemptRequest.AnswerSubmission(1L, 100L)));

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class,
                () -> quizAttemptService.submitAttempt("unknown@example.com", request));

        verify(subcategoryRepository, never()).findById(any());
        verify(quizAttemptRepository, never()).save(any());
    }

    @Test
    void submitAttempt_withUnknownSubcategory_throwsRecordNotFoundException() {
        // Arrange
        User user = buildUser();

        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(999L);
        request.setAnswers(List.of(new QuizAttemptRequest.AnswerSubmission(1L, 100L)));

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class,
                () -> quizAttemptService.submitAttempt("bram@example.com", request));

        verify(quizAttemptRepository, never()).save(any());
    }

    @Test
    void calculateScore_withAllCorrect_returnsFullScore() {
        // Arrange — 3 correct answers
        User user = buildUser();
        Subcategory subcategory = buildSubcategory();

        List<QuizAttemptRequest.AnswerSubmission> submissions = List.of(
                new QuizAttemptRequest.AnswerSubmission(1L, 101L),
                new QuizAttemptRequest.AnswerSubmission(2L, 102L),
                new QuizAttemptRequest.AnswerSubmission(3L, 103L)
        );
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategory.getId());
        request.setAnswers(submissions);

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory));
        when(answerRepository.findById(101L)).thenReturn(Optional.of(correctAnswer(101L)));
        when(answerRepository.findById(102L)).thenReturn(Optional.of(correctAnswer(102L)));
        when(answerRepository.findById(103L)).thenReturn(Optional.of(correctAnswer(103L)));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(inv -> inv.getArgument(0));
        when(quizAttemptRepository.findByUserId(1L)).thenReturn(List.of());
        when(badgeService.checkAndAwardBadges(any(), anyInt(), anyInt(), anyInt(), any())).thenReturn(List.of());

        // Act
        QuizAttemptResponse response = quizAttemptService.submitAttempt("bram@example.com", request);

        // Assert
        assertEquals(3, response.getScore());
        assertEquals(3, response.getTotalQuestions());
    }

    @Test
    void calculateScore_withNullAnswerId_skipsAnswer() {
        // Arrange — one answered (correct) and one unanswered (null)
        User user = buildUser();
        Subcategory subcategory = buildSubcategory();

        List<QuizAttemptRequest.AnswerSubmission> submissions = List.of(
                new QuizAttemptRequest.AnswerSubmission(1L, 101L),
                new QuizAttemptRequest.AnswerSubmission(2L, null)
        );
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategory.getId());
        request.setAnswers(submissions);

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory));
        when(answerRepository.findById(101L)).thenReturn(Optional.of(correctAnswer(101L)));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(inv -> inv.getArgument(0));
        when(quizAttemptRepository.findByUserId(1L)).thenReturn(List.of());
        when(badgeService.checkAndAwardBadges(any(), anyInt(), anyInt(), anyInt(), any())).thenReturn(List.of());

        // Act
        QuizAttemptResponse response = quizAttemptService.submitAttempt("bram@example.com", request);

        // Assert — 1 correct; null answer skipped (answerRepository never called for it)
        assertEquals(1, response.getScore());
        verify(answerRepository, never()).findById(null);
    }

    @Test
    void submitAttempt_withNoAnswers_setsPercentageToZero() {
        // Arrange
        User user = buildUser();
        Subcategory subcategory = buildSubcategory();

        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategory.getId());
        request.setAnswers(List.of());

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(inv -> inv.getArgument(0));
        when(quizAttemptRepository.findByUserId(1L)).thenReturn(List.of());
        when(badgeService.checkAndAwardBadges(any(), anyInt(), anyInt(), anyInt(), any())).thenReturn(List.of());

        // Act
        QuizAttemptResponse response = quizAttemptService.submitAttempt("bram@example.com", request);

        // Assert
        assertEquals(0, response.getScore());
        assertEquals(0, response.getTotalQuestions());
        assertEquals(0.0, response.getPercentage());
    }

    @Test
    void submitAttempt_awardsBadgesOnPerfectScore() {
        // Arrange
        User user = buildUser();
        Subcategory subcategory = buildSubcategory();

        Badge perfectBadge = new Badge("PERFECT_SCORE", "Perfecte score behaald", "PERFECT_SCORE");
        perfectBadge.setId(5L);

        QuizAttemptRequest.AnswerSubmission submission = new QuizAttemptRequest.AnswerSubmission(1L, 101L);
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategory.getId());
        request.setAnswers(List.of(submission));

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory));
        when(answerRepository.findById(101L)).thenReturn(Optional.of(correctAnswer(101L)));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(inv -> {
            QuizAttempt saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        when(quizAttemptRepository.findByUserId(1L)).thenReturn(List.of());
        when(badgeService.checkAndAwardBadges(eq(user), anyInt(), anyInt(), anyInt(), eq(Difficulty.BEGINNER)))
                .thenReturn(List.of(perfectBadge));

        // Act
        QuizAttemptResponse response = quizAttemptService.submitAttempt("bram@example.com", request);

        // Assert
        assertEquals(1, response.getEarnedBadges().size());
        assertEquals("PERFECT_SCORE", response.getEarnedBadges().get(0).getName());
        verify(badgeService).checkAndAwardBadges(eq(user), anyInt(), anyInt(), anyInt(), eq(Difficulty.BEGINNER));
    }
}
