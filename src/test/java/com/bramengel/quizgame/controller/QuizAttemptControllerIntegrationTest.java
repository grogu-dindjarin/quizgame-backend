package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.QuizAttemptRequest;
import com.bramengel.quizgame.dto.QuizAttemptRequest.AnswerSubmission;
import com.bramengel.quizgame.model.Category;
import com.bramengel.quizgame.model.Difficulty;
import com.bramengel.quizgame.model.Role;
import com.bramengel.quizgame.model.Subcategory;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.model.UserProfile;
import com.bramengel.quizgame.repository.CategoryRepository;
import com.bramengel.quizgame.repository.SubcategoryRepository;
import com.bramengel.quizgame.repository.UserProfileRepository;
import com.bramengel.quizgame.repository.UserRepository;
import com.bramengel.quizgame.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class QuizAttemptControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private String userToken;
    private Long subcategoryId;

    @BeforeEach
    void setUp() {
        User user = new User("quizplayer@example.com", passwordEncoder.encode("pass1234"), Role.USER);
        userRepository.save(user);

        UserProfile profile = new UserProfile(user, "Quiz Player");
        userProfileRepository.save(profile);

        userToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        Category category = new Category("Science");
        categoryRepository.save(category);

        Subcategory subcategory = new Subcategory("Biology", Difficulty.BEGINNER, category);
        subcategoryRepository.save(subcategory);

        subcategoryId = subcategory.getId();
    }

    @Test
    void submitAttempt_withValidToken_returns200() throws Exception {
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategoryId);
        request.setAnswers(List.of(new AnswerSubmission(null, null)));

        mockMvc.perform(post("/api/quiz-attempts")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.subcategoryName").value("Biology"));
    }

    @Test
    void submitAttempt_withoutToken_returns403() throws Exception {
        QuizAttemptRequest request = new QuizAttemptRequest();
        request.setSubcategoryId(subcategoryId);
        request.setAnswers(List.of(new AnswerSubmission(null, null)));

        mockMvc.perform(post("/api/quiz-attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
